import {Component, ViewChild} from '@angular/core';
import {MatSidenav} from "@angular/material/sidenav";
import {AuthService} from "./service/auth.service";
import {Router} from "@angular/router";
import {User} from "./models/User";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'farm-erp-ui';

  userName = '' ;

  @ViewChild(MatSidenav, {static: false}) sidenav!: MatSidenav;

  constructor(
    private authService: AuthService,
    private router : Router,
  ) {
    this.authService.currentUser.subscribe(
      user => {
        this.userName = sessionStorage.getItem('name') ?? ''
      }
    )
  }

  onRout() {
    this.sidenav.close();
  }

  logout() {
    this.authService.logout();
  }
}
