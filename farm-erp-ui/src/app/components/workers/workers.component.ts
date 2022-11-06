import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {DialogService} from "../../service/dialog.service";
import {Worker} from "../../models/worker/worker";
import {WorkersService} from "../../service/workers.service";
import {RoleService} from "../../service/role.service";
import {PageRequestWorkers} from "../../models/page-requests/page-request-workers";

@Component({
  selector: 'app-workers',
  templateUrl: './workers.component.html',
  styleUrls: ['./workers.component.scss']
})
export class WorkersComponent implements OnInit {


  dataSource: MatTableDataSource<Worker>;
  allNumber: number;
  pageRequest: PageRequestWorkers;

  constructor(
    private workersService: WorkersService,
    private dialogService: DialogService,
    public roleService: RoleService,
  ) {
    this.dataSource = new MatTableDataSource();
    this.allNumber = 0;
    this.pageRequest = {
      page: 0,
      pageSize: 10,
      sortBy: "",
      direction: "",

      name: "",
      vehicle: -1,
      position: "",
      createdAtFrom: "",
      createdAtTo: ""
    }
  }

  ngOnInit(): void {
    this.workersService.getALlWorkers(this.pageRequest).subscribe(
      data => {
        this.dataSource.data = data.data.objects[0];
        this.allNumber = data.data.allNumber;
      });
  }

  update(worker: Worker) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.workersService.openUpdateDialog(Object.assign({}, worker)).subscribe(
      data => {
        if (data) {
          this.workersService.updateWorker(data).subscribe(
            data => {
              console.log(data);
              if (data.code === 200) {
                this.dialogService.openDialog("Sikeres frissítés", "info");
              } else {
                this.dialogService.openDialog("Sikertelen frissítés", "info");
              }
              this.ngOnInit();
            });
        }
      });
  }

  delete(worker: Worker) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.dialogService.openConfirmDialog("Biztosan szeretné törölni?", "warn").afterClosed().subscribe(res => {
      if (res)
        this.workersService.deleteWorker(worker.id).subscribe(
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

  create(worker: Worker) {
    this.workersService.createNewWorker(worker).subscribe(
      data => {
        console.log(data);
        if (data.code === 200) {
          this.dialogService.openDialog("Sikeresen létrehozva", "info");
        } else {
          this.dialogService.openDialog("Sikertelen művelet", "info");
        }
        this.ngOnInit();
      });
  }

  search(worker: Worker) {
    console.log(worker);
    this.pageRequest.name = worker.name;
    this.pageRequest.position = worker.position;
    this.pageRequest.vehicle = worker.vehicle?.id ?? -1;

    this.ngOnInit();
  }

  clear() {
    this.pageRequest.name = '';
    this.pageRequest.position = '';
    this.pageRequest.vehicle = -1;

    this.pageRequest.page = 0;
    this.pageRequest.pageSize = 10;

    this.ngOnInit();
  }
}
