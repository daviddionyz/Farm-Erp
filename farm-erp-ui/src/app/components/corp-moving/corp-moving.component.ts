import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {RoleService} from "../../service/role.service";
import {HarvestService} from "../../service/harvest.service";
import {DialogService} from "../../service/dialog.service";
import {Diary} from "../../models/harvest/Diary";
import {MatSelectionList} from "@angular/material/list";
import {MatTableDataSource} from "@angular/material/table";
import {Deliveries} from "../../models/harvest/Deliveries";
import {DeliveriesService} from "../../service/deliveries.service";
import {DatePipe} from "@angular/common";
import {PageEvent} from "@angular/material/paginator";
import {CorpMovingService} from "../../service/corp-moving.service";

@Component({
  selector: 'app-corp-moving',
  templateUrl: './corp-moving.component.html',
  styleUrls: ['./corp-moving.component.scss']
})
export class CorpMovingComponent implements OnInit {

  pageSize = 10;
  pageIndex = 0;
  length: any;
  pageSizeOptions = [5, 10, 20];

  datepipe: DatePipe = new DatePipe('en-US');


  displayedColumns: string[] = [
    'worker', 'vehicle', 'gross', 'empty', 'net', 'from', 'where', 'intakeDate', 'update', 'delete'
  ];

  dataSource: MatTableDataSource<Deliveries>
  searchCriteria: any;

  constructor(
    private corpMovingService: CorpMovingService,
    private deliveriesService: DeliveriesService,
    private dialogService: DialogService,
    private roleService: RoleService,
  ) {
    this.dataSource = new MatTableDataSource<Deliveries>();
  }

  ngOnInit(): void {
    this.corpMovingService.getDeliveriesCorpMoving(this.searchCriteria, this.pageIndex, this.pageSize).subscribe(
      deliveries => {
        if (deliveries.code === 200) {
          this.dataSource.data = deliveries.data.objects[0];
          this.length = deliveries.data.allNumber;
        }
      });
  }

  update(deliveries: Deliveries) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.deliveriesService.openCreateDialog(null, true, deliveries).subscribe(
      data => {
        if (data) {
          data.intakeDate = String(this.datepipe.transform(data.intakeDate, 'yyyy-MM-dd HH:mm:ss') ?? '');
          this.deliveriesService.updateDelivery(data).subscribe(
            data => {
              if (data.code === 200 || data.code === 502) {
                this.dialogService.openSuccessDialog();
              } else {
                this.dialogService.openUnexpectedErrorDialog();
              }
              this.ngOnInit();
            });
        }
      });
  }

  delete(deliveries: Deliveries) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.dialogService.openConfirmDialog("Biztosan szeretné törölni?", "warn").afterClosed().subscribe(res => {
      if (res)
        this.deliveriesService.deleteDelivery(deliveries.id).subscribe(
          data => {
            if (data.code === 200) {
              this.dialogService.openSuccessDialog();
            } else {
              this.dialogService.openUnexpectedErrorDialog();
            }
            this.ngOnInit();
          });
    });
  }

  pageEvent(page: PageEvent) {
    this.pageIndex = page.pageIndex;
    this.pageSize = page.pageSize;

    this.ngOnInit();
  }

  search() {
    this.ngOnInit();
  }

  clear() {
    this.searchCriteria = "";
    this.pageIndex = 0;
    this.pageSize = 10;

    this.ngOnInit();
  }

  createDeliver() {
    this.deliveriesService.openCreateDialog(null, true).subscribe(
      data => {
        if (data) {
          data.intakeDate = String(this.datepipe.transform(data.intakeDate, 'yyyy-MM-dd HH:mm:ss') ?? '');
          this.deliveriesService.createNewDelivery(data).subscribe(
            data => {
              if (data.code === 200 || data.code === 502) {
                this.dialogService.openSuccessDialog();
              } else {
                this.dialogService.openUnexpectedErrorDialog();
              }
              this.ngOnInit();
            });
        }
      });
  }
}
