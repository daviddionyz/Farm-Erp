import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {DialogComponent} from "../shared/dialog/dialog.component";
import {ValidationDialogComponent} from "../shared/validation-dialog/validation-dialog.component";
import {SpinnerComponent} from "../shared/spinner/spinner.component";
import {NgxSpinnerService} from "ngx-spinner";

@Injectable({providedIn: 'root'})
export class DialogService {

  spinner : any

  constructor(private router: Router,
              private dialog: MatDialog,
              ) {
  }

  openDialog(message: string, type: string, status?: number) {
    const dialogConfig = new MatDialogConfig();

    if (type === 'warn' && status) {
      message = this.setErrorMessage(status);
    }

    dialogConfig.data = {message, type};
    this.dialog.open(DialogComponent, dialogConfig);
  }

  openUnexpectedErrorDialog(){
    this.openDialog("Váratlan hiba történt", "info");
  }

  openSuccessDialog(){
    this.openDialog("Sikeres művelet", "info");
  }

  openConfirmDialog(message: string, type: string, title?: string) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = {message, type, title, isConfirm: true};
    dialogConfig.width = '250px';
    return this.dialog.open(DialogComponent, dialogConfig);
  }

  openValidationDialog(messages: string[], type: string, title?: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {messages, type, title, isConfirm: true};

    return this.dialog.open(ValidationDialogComponent, dialogConfig);
  }

  // @ts-ignore
  showSpinner(): MatDialogRef<SpinnerComponent> {
    if (!this.spinner) {
      this.spinner = this.dialog.open(SpinnerComponent, {
        disableClose: true,
      });

      return this.spinner;
    }
  }

  stopSpinner() {
    if (this.spinner) {
      this.spinner.close();
      this.spinner = null;
    }
  }

  private setErrorMessage(status: number) {
    switch (status) {
      case 404:
        return 'A keresett oldal nem található';
      case 500:
        return 'Kapcsolódási hiba!';
      case 401:
        return 'A token lejárt! Kérlek jelentkezz be újból.';
      default:
        return 'Váratlan hiba!';
    }
  }

}



