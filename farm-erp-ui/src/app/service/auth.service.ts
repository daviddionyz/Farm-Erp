import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {distinctUntilChanged, map} from "rxjs/operators";
import {Router} from "@angular/router";
import {DialogService} from "./dialog.service";
import {environment} from "../../environments/environment";
import {User} from "../models/User";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUserSubject!: BehaviorSubject<User>;
  public currentUser!: Observable<User>;

  constructor(private http: HttpClient,
              private router: Router,
              private dialogService: DialogService,
  ) {
    this.setUser();
  }

  setUser() {
    const token = sessionStorage.getItem('token');
    let user: User;

    if (token) {
      user = JSON.parse(atob(token.split('.')[1]));
    }

    // @ts-ignore
    this.currentUserSubject = new BehaviorSubject<User>(user);
    this.currentUser = this.currentUserSubject.asObservable().pipe(distinctUntilChanged());
  }

  login(user: { username: string, password: string }) {

    // const baseCode = 'Basic ' + btoa(username + '$' + password);
    // const headers = new HttpHeaders({Authorization: baseCode});

    return this.http.post<any>(`${environment.url}/auth/login`, user).pipe(
      map(response => {
          console.log(response)
          if (response.code === 200) {
            sessionStorage.setItem('token', 'Bearer ' + response.data.token);
            sessionStorage.setItem('name', response.data.name);
            sessionStorage.setItem('role', response.data.role);
            this.currentUserSubject.next(response.data);
            return response;
          }else{
            this.dialogService.openDialog("Sikertelen bejelntekezés!", "info");
          }

        }
      )
    )


  }

  logout() {
    sessionStorage.clear();
    // @ts-ignore
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }

  isAuthorized(): boolean {

    const token = sessionStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/']);
      return false;
    }

    const decodeToken: any = JSON.parse(atob(token.split('.')[1]));

    if (!decodeToken) {
      console.log(`Token has benn expired, log in again`);
      this.dialogService.openDialog('Hibás token! Jelentkez be újból.', 'info');
      this.logout();
      return false;
    }

    if (this.tokenExpired(decodeToken.exp)) {
      console.log(`Token has benn expired, log in again`);
      this.dialogService.openDialog('A token lejárt! Jelentkez be újból.', 'info');
      this.logout();
      return false;
    }

    return true;
  }

  private tokenExpired(expire: number) {
    return (Math.floor((new Date).getTime() / 1000)) >= expire;
  }
}
