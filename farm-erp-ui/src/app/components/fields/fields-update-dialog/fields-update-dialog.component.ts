import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Storage} from "../../../models/storage/storage";
import {Field} from "../../../models/fields/field";

@Component({
  selector: 'app-fields-update-dialog',
  templateUrl: './fields-update-dialog.component.html',
  styleUrls: ['./fields-update-dialog.component.scss']
})
export class FieldsUpdateDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<FieldsUpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Field) {
  }

  isDataValid() {
    return !(this.data.corpType !== ''
      && this.data.corpName !== ''
      && this.data.name !== ''
      && this.data.size >= 0 && this.data.size !== null
      && this.data.size > 0
      && this.data.size <= 2147483647)
  }
}
