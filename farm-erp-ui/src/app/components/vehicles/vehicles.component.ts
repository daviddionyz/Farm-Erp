import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Field} from "../../models/fields/field";
import {FieldsService} from "../../service/fields.service";
import {DialogService} from "../../service/dialog.service";
import {VehiclesService} from "../../service/vehicles.service";
import {Vehicles} from "../../models/vehicles/vehicles";
import {RoleService} from "../../service/role.service";
import {PageRequestStorage} from "../../models/page-requests/page-request-storage";
import {PageRequestVehicles} from "../../models/page-requests/page-request-vehicles";

@Component({
  selector: 'app-vehicles',
  templateUrl: './vehicles.component.html',
  styleUrls: ['./vehicles.component.scss']
})
export class VehiclesComponent implements OnInit {

  dataSource: MatTableDataSource<Vehicles>;
  pageRequest: PageRequestVehicles
  allNumber: number;

  constructor(
    private vehiclesService: VehiclesService,
    private dialogService: DialogService,
    public roleService: RoleService,
  ) {
    this.dataSource = new MatTableDataSource();
    this.allNumber = 0;
    this.pageRequest = {
      page: 0,
      pageSize: 20,
      sortBy: "",
      direction: "",

      name: '',
      status: -1,
      type: ""
    }
  }

  ngOnInit(): void {
    this.vehiclesService.getALlVehicles(this.pageRequest).subscribe(
      data => {
        console.log(data);
        this.dataSource.data = data.data.objects[0];
        this.allNumber = data.data.allNumber;
      });
  }

  update(vehicles: Vehicles) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.vehiclesService.openUpdateDialog(Object.assign({}, vehicles)).subscribe(
      updateDialogRes => {
        if (updateDialogRes) {
          this.vehiclesService.updateVehicle(updateDialogRes).subscribe(
            apiRes => {
              console.log(apiRes);
              if (apiRes.code === 200) {
                this.dialogService.openDialog("Sikeres frissítés", "info");
              } else {
                this.dialogService.openDialog("Sikertelen frissítés", "info");
              }
              this.ngOnInit();
            });
        }
      });
  }

  delete(vehicles: Vehicles) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.dialogService.openConfirmDialog("Biztosan szeretné törölni?", "warn").afterClosed().subscribe(res => {
      if (res)
        this.vehiclesService.deleteVehicle(vehicles.id).subscribe(
          data => {
            console.log(data);
            if (data.code === 200) {
              this.dialogService.openDialog("Sikeres törlés", "info");
            } else {
              this.dialogService.openDialog("Sikertelen törlés", "info");
            }
            this.ngOnInit();
          });
    });
  }

  create(vehicles: Vehicles) {
    this.vehiclesService.createNewVehicle(vehicles).subscribe(
      data => {
        console.log(data);
        if (data.code === 200) {
          this.dialogService.openDialog("Sikeresen létrehozva", "info");
        } else {
          this.dialogService.openDialog("Sikertelen művelet", "info");
        }
        this.ngOnInit();
      }
    )
  }

  search(vehicles: Vehicles) {
    this.pageRequest.name = vehicles.name;
    this.pageRequest.status = vehicles.status;
    this.pageRequest.type = vehicles.type;

    this.ngOnInit();
  }

  clear() {
    this.pageRequest.name = '';
    this.pageRequest.status = Number.NaN;
    this.pageRequest.type = '';

    this.pageRequest.page = 0;
    this.pageRequest.pageSize = 10;
    this.ngOnInit();
  }


}
