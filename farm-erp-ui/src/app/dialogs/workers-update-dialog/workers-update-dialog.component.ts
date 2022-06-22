import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Worker} from "../../models/worker/worker";
import {Vehicles} from "../../models/vehicles/vehicles";
import {VehiclesService} from "../../service/vehicles.service";

@Component({
  selector: 'app-workers-update-dialog',
  templateUrl: './workers-update-dialog.component.html',
  styleUrls: ['./workers-update-dialog.component.scss']
})
export class WorkersUpdateDialogComponent implements OnInit {
  vehicles: Vehicles[] = [];

  constructor(
    public dialogRef: MatDialogRef<WorkersUpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Worker,
    private vehicleService: VehiclesService
  ) {
    vehicleService.getALlVehicleWithoutPageRequest().subscribe(
      vehicles => {
        this.vehicles = vehicles.data;
      }
    )
  }

  ngOnInit(): void {
  }

  onNoClick() {
    console.log(this.data);
    this.dialogRef.close()
  }


}
