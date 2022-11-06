import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Storage} from "../../../models/storage/storage";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Field} from "../../../models/fields/field";
import {PageRequestFields} from "../../../models/page-requests/page-request-fields";

@Component({
  selector: 'app-field-table',
  templateUrl: './field-table.component.html',
  styleUrls: ['./field-table.component.scss']
})
export class FieldTableComponent {

  pageSize  = 10;
  pageIndex = 0;
  pageSizeOptions = [5,10,20];

  displayedColumns: string[] = [
    'name', 'size', 'corpType', 'corpName' , 'update', 'delete'
  ];

  @Input() dataSource!: MatTableDataSource<Field>;
  @Input() length!: number;
  @Input() pageRequest! : PageRequestFields;

  @Output() updateEvent: EventEmitter<Field> = new EventEmitter();
  @Output() deleteEvent: EventEmitter<Field> = new EventEmitter();
  @Output() page  : EventEmitter<any>   = new EventEmitter();

  constructor() {}

  update(fieldDTO: Field) {
    this.updateEvent.emit(fieldDTO);
  }

  delete(fieldDTO : Field) {
    this.deleteEvent.emit(fieldDTO);
  }

  pageEvent(page: PageEvent) {
    this.pageIndex = page.pageIndex;
    this.pageSize  = page.pageSize;

    this.pageRequest.page     = this.pageIndex;
    this.pageRequest.pageSize = this.pageSize;

    this.page.emit();
  }
}
