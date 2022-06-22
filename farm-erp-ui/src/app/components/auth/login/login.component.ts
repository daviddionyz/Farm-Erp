import { Component, OnInit } from '@angular/core';
import {DialogService} from "../../../service/dialog.service";
import {Router} from "@angular/router";
import {AuthService} from "../../../service/auth.service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide = true;

  username = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private dialogServie: DialogService,
    ) {
  }

  ngOnInit(): void {
  }

  async onLogin() {

    console.log("login started...");
    if (this.username === '' && this.password === '' )
    {
      this.dialogServie.openDialog('Hiányos adatok!','info');
      return;
    }
    if (this.username === '')
    {
      this.dialogServie.openDialog('Felhasználónév nem lett megadva!','info');
      return;
    }
    if ( this.password === '' )
    {
      this.dialogServie.openDialog('Jelszó nem lett megadva','info');
      return;
    }

    this.authService.login({username: this.username , password : this.password}).subscribe(
      data => {
        if (data){
          console.log(`${sessionStorage.getItem('name')} user has been logged in` );
          this.router.navigate(['/welcome']);
        }
      },
      error => console.log('Auth error', error),
    );

    // this.router.navigate(['/welcome']);
  }
}
