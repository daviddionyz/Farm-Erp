import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";



@Component({
  selector: 'app-validation-dialog',
  templateUrl: './validation-dialog.component.html',
  styleUrls: ['./validation-dialog.component.scss']
})
export class ValidationDialogComponent {

  color;
  isConfirm: any;

  constructor(public dialogRef: MatDialogRef<ValidationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { messages: string[], type: string, title?: string}) {

    dialogRef.addPanelClass(data.type);
    this.color = data.type === 'info' ? (this.isConfirm ? 'warn' : 'accent') : 'warn';
  }

  onOkClick() {
    this.dialogRef.close(true);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
