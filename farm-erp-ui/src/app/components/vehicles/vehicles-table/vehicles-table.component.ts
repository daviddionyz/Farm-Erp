import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Field} from "../../../models/fields/field";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Vehicles} from "../../../models/vehicles/vehicles";
import {PageRequestStorage} from "../../../models/page-requests/page-request-storage";

@Component({
  selector: 'app-vehicles-table',
  templateUrl: './vehicles-table.component.html',
  styleUrls: ['./vehicles-table.component.scss']
})
export class VehiclesTableComponent implements OnInit {

  pageSize  = 10;
  pageIndex = 0;
  pageSizeOptions = [5,10,20];

  displayedColumns: string[] = [
    'name', 'type', 'status' , 'update', 'delete'
  ];

  @Input() dataSource!: MatTableDataSource<Vehicles>;
  @Input() pageRequest!: PageRequestStorage;
  @Input() length!: number;

  @Output() updateEvent: EventEmitter<Vehicles> = new EventEmitter();
  @Output() deleteEvent: EventEmitter<Vehicles> = new EventEmitter();
  @Output() page  : EventEmitter<any>   = new EventEmitter();

  constructor() {
  }

  ngOnInit(): void {

  }

  update(vehicles: Vehicles) {
    this.updateEvent.emit(vehicles);
  }

  delete(vehicles : Vehicles) {
    this.deleteEvent.emit(vehicles);
  }

  pageEvent(page: PageEvent) {
    this.pageIndex = page.pageIndex;
    this.pageSize  = page.pageSize;

    this.pageRequest.page     = this.pageIndex;
    this.pageRequest.pageSize = this.pageSize;

    this.page.emit();
  }

  statusNumToText(statusNum: number){
    switch (statusNum) {
      case 1:
        return "Szervíz alatt";
      case 2:
        return "Foglalt";
      case 3:
        return "Nem használt";
      default:
        return "ismeretlen";
    }
  }
}
