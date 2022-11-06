import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Field} from "../../../models/fields/field";
import {Diary} from "../../../models/harvest/Diary";

@Component({
  selector: 'app-harvest-diary-create-dialog',
  templateUrl: './harvest-diary-create-dialog.component.html',
  styleUrls: ['./harvest-diary-create-dialog.component.scss']
})
export class HarvestDiaryCreateDialogComponent {


  constructor(
    public dialogRef: MatDialogRef<HarvestDiaryCreateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Diary ) {}

  isDisabled(){
    return this.data.name === '' || this.data.name === null
      || isNaN(this.data.year) || this.data.year === null;
  }
}
