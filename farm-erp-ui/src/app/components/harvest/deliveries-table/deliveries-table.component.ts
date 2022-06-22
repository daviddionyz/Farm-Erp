import {Component, Input, OnInit} from '@angular/core';
import {Diary} from "../../../models/harvest/Diary";
import {DeliveriesService} from "../../../service/deliveries.service";
import {MatTableDataSource} from "@angular/material/table";
import {DialogService} from "../../../service/dialog.service";
import {RoleService} from "../../../service/role.service";
import {Deliveries} from "../../../models/harvest/Deliveries";
import {PageEvent} from "@angular/material/paginator";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-deliveries-table',
  templateUrl: './deliveries-table.component.html',
  styleUrls: ['./deliveries-table.component.scss']
})
export class DeliveriesTableComponent implements OnInit {

  pageSize  = 10;
  pageIndex = 0;
  length: any;
  pageSizeOptions = [5,10,20];

  displayedColumns: string[] = [
    'worker', 'vehicle', 'gross' , 'empty', 'net', 'from', 'where', 'intakeDate', 'update', 'delete'
  ];

  dataSource : MatTableDataSource<Deliveries>
  @Input() diary!: Diary | null
  searchCriteria: any;

  constructor(
    private deliveriesService : DeliveriesService,
    private dialogService     : DialogService,
    private roleService       : RoleService,
  ) {
    this.dataSource = new MatTableDataSource<Deliveries>();
  }

  ngOnInit(): void {
    this.deliveriesService.getDeliveriesByDiary(this.diary?.id ?? -1,this.searchCriteria,this.pageIndex,this.pageSize).subscribe(
      deliveries => {
        console.log(deliveries);
        if (deliveries.code === 200){
          this.dataSource.data = deliveries.data.objects[0];
          this.length = deliveries.data.allNumber;
        }
      }
    )
  }

  update(deliveries: Deliveries) {
    if (this.roleService.openPopUpIfNotBoss()){
      return
    }

    this.deliveriesService.openCreateDialog(this.diary?.id ?? -1,false, deliveries).subscribe(
      data => {
        console.log(data);
        if (data){
          const datepipe: DatePipe = new DatePipe('en-US');

          data.intakeDate = String(datepipe.transform(data.intakeDate, 'yyyy-MM-dd HH:mm:ss') ?? '');
          this.deliveriesService.updateDelivery(data).subscribe(
            data => {
              if(data.code === 200 || data.code === 502){
                this.dialogService.openSuccessDialog();
              }else{
                this.dialogService.openUnexpectedErrorDialog();
              }
              this.ngOnInit();
            }
          )
        }
      }
    )


  }

  delete(deliveries: Deliveries) {
    if (this.roleService.openPopUpIfNotBoss()){
      return
    }

    this.deliveriesService.deleteDelivery(deliveries.id).subscribe(
      data => {
        if(data.code === 200){
          this.dialogService.openSuccessDialog();
        }else{
          this.dialogService.openUnexpectedErrorDialog();
        }
        this.ngOnInit();
      }
    )
  }

  pageEvent(page: PageEvent) {
    this.pageIndex = page.pageIndex;
    this.pageSize  = page.pageSize;

    this.ngOnInit();
  }

  search() {
    this.ngOnInit();
  }

  clear() {
    this.searchCriteria = "";
    this.pageIndex      = 0;
    this.pageSize       = 10;

    this.ngOnInit();
  }

  createDeliver() {
    this.deliveriesService.openCreateDialog(this.diary?.id ?? -1).subscribe(
      data => {
        console.log(data);
        if (data){
          const datepipe: DatePipe = new DatePipe('en-US');

          data.intakeDate = String(datepipe.transform(data.intakeDate, 'yyyy-MM-dd HH:mm:ss') ?? '');
          this.deliveriesService.createNewDelivery(data).subscribe(
            data => {
              if(data.code === 200 || data.code === 502){
                this.dialogService.openSuccessDialog();
              }else{
                this.dialogService.openUnexpectedErrorDialog();
              }
              this.ngOnInit();
            }
          )
        }
      }
    )
  }


}
