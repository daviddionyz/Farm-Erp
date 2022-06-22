import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Storage} from "../../models/storage/storage";

@Component({
  selector: 'app-storage-update-dialog',
  templateUrl: './storage-update-dialog.component.html',
  styleUrls: ['./storage-update-dialog.component.scss']
})
export class StorageUpdateDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<StorageUpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Storage
  ) {
  }

  ngOnInit(): void {
  }

  onNoClick() {
    this.dialogRef.close()
  }
}
