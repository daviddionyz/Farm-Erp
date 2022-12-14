import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Storage} from "../../../models/storage/storage";

@Component({
  selector: 'app-storage-update-dialog',
  templateUrl: './storage-update-dialog.component.html',
  styleUrls: ['./storage-update-dialog.component.scss']
})
export class StorageUpdateDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<StorageUpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Storage ) {}

  isDataValid() {
    return !(this.data.name !== ''
      && this.data.capacity !== null
      && this.data.capacity > 0
      && this.data.capacity > 0
      && this.data.capacity <= 2147483647)
  }
}
