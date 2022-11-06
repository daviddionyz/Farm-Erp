import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Storage} from "../../../models/storage/storage";
import {PageRequestStorage} from "../../../models/page-requests/page-request-storage";
import {PageEvent} from "@angular/material/paginator";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Diary} from "../../../models/harvest/Diary";
import {Crops} from "../../../models/storage/Crops";

@Component({
  selector: 'app-storage-crops-dialog',
  templateUrl: './storage-crops-dialog.component.html',
  styleUrls: ['./storage-crops-dialog.component.scss']
})
export class StorageCropsDialogComponent {

  pageSize        = 10;
  pageIndex       = 0;

  pageSizeOptions = [5,10,20];

  displayedColumns: string[] = [
    'cropName', 'cropType', 'amount'
  ];

  dataSource : MatTableDataSource<Crops> = new MatTableDataSource<Crops>();

  constructor(
    public dialogRef: MatDialogRef<StorageCropsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Crops[]
  ) {
    this.dataSource.data = data
  }

}
