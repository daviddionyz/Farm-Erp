import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {EMPTY, Observable, throwError} from 'rxjs';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {catchError} from "rxjs/operators";
import {AuthService} from "../service/auth.service";
import {DialogService} from "../service/dialog.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private router: Router,
              private dialog: MatDialog,
              private autService: AuthService,
              private dialogService: DialogService,
              ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = sessionStorage.getItem('token');
    console.log(`Jwt interceptor request url: $`,request.url);
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: token
        }
      });
    }

    if (!request.headers.has('Content-Type') && request.url.includes('/confirm') ) {
      request = request.clone({
        headers: request.headers.set('Content-Type', 'application/json')
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        const errorMessage: string = error.error.message || `Error Code: ${error.status}\nMessage: ${error.message}`;

        if (error.status === 401) {
          this.dialog.closeAll();

          if (errorMessage.includes('JWT token is expired')) {
            this.dialogService.openDialog('A token lejárt! Kérlek jelentkezz be újból.', 'info');
          } else if (errorMessage.includes('Bad credentials')) {
            this.dialogService.openDialog('Hibás felhasználónév vagy jelszó.', 'info');
          }

          this.autService.logout();
          return EMPTY;
        } else {
          return throwError(error.error);
        }
      })
    );
  }
}
