import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Storage} from "../../models/storage/storage";
import {Vehicles} from "../../models/vehicles/vehicles";
import {VehiclesService} from "../../service/vehicles.service";
import {Worker} from "../../models/worker/worker";

@Component({
  selector: 'app-vehicles-update-dialog',
  templateUrl: './vehicles-update-dialog.component.html',
  styleUrls: ['./vehicles-update-dialog.component.scss']
})
export class VehiclesUpdateDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<VehiclesUpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Vehicles
  ) {
  }

  ngOnInit(): void {
  }

  onNoClick() {
    this.dialogRef.close()
  }

}
