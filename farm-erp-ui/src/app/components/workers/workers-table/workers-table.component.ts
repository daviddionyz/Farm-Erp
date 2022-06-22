import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Vehicles} from "../../../models/vehicles/vehicles";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Worker} from "../../../models/worker/worker";
import {PageRequestStorage} from "../../../models/page-requests/page-request-storage";

@Component({
  selector: 'app-workers-table',
  templateUrl: './workers-table.component.html',
  styleUrls: ['./workers-table.component.scss']
})
export class WorkersTableComponent implements OnInit {

  pageSize  = 10;
  pageIndex = 0;
  pageSizeOptions = [5,10,20];

  displayedColumns: string[] = [
    'name', 'joinDate', 'vehicleId' , 'position', 'update', 'delete'
  ];

  @Input() dataSource!: MatTableDataSource<Worker>;
  @Input() pageRequest!: PageRequestStorage;
  @Input() length!: number;

  @Output() updateEvent: EventEmitter<Worker> = new EventEmitter();
  @Output() deleteEvent: EventEmitter<Worker> = new EventEmitter();
  @Output() page  : EventEmitter<any>   = new EventEmitter();

  constructor() {
  }

  ngOnInit(): void {

  }

  update(worker: Worker) {
    this.updateEvent.emit(worker);
  }

  delete(worker : Worker) {
    this.deleteEvent.emit(worker);
  }

  getVehicleName(vehicle: Vehicles) {
    if (vehicle){
      return vehicle.name;
    }else {
      return '';
    }
  }

  pageEvent(page: PageEvent) {
    this.pageIndex = page.pageIndex;
    this.pageSize  = page.pageSize;

    this.pageRequest.page     = this.pageIndex;
    this.pageRequest.pageSize = this.pageSize;

    this.page.emit();
  }
}
