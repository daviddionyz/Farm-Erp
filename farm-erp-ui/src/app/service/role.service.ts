import {Injectable, OnInit} from '@angular/core';
import {AuthService} from "./auth.service";
import {Roles} from "../enums/Roles";
import {User} from "../models/User";
import {DialogService} from "./dialog.service";

@Injectable({
  providedIn: 'root'
})
export class RoleService implements OnInit{

  role!  : number

  constructor(
    private authService:   AuthService,
    private dialogService: DialogService,
  ) {
    this.authService.currentUser.subscribe(
      user => {
          this.role = Number(sessionStorage.getItem('role'));
      }
    )
  }

  ngOnInit(): void {

  }

  checkIsItBoss(): boolean {
    return this.role === Roles.BOSS || this.role === Roles.SMALL_BOSS
  }

  openPopUpIfNotBoss(){
    if (!this.checkIsItBoss()){
      this.dialogService.openDialog("Ehez a művelethez nincs jogosultsága!","info");
      return true;
    }
    return false
  }


}
