import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Field} from "../../models/fields/field";
import {Diary} from "../../models/harvest/Diary";

@Component({
  selector: 'app-harvest-diary-create-dialog',
  templateUrl: './harvest-diary-create-dialog.component.html',
  styleUrls: ['./harvest-diary-create-dialog.component.scss']
})
export class HarvestDiaryCreateDialogComponent implements OnInit {


  constructor(
    public dialogRef: MatDialogRef<HarvestDiaryCreateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Diary
  ) {
  }

  ngOnInit(): void {
  }

  onNoClick() {
    this.dialogRef.close()
  }

}
