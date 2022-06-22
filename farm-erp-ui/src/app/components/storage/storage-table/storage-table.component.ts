import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Storage} from "../../../models/storage/storage";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {PageRequestStorage} from "../../../models/page-requests/page-request-storage";

@Component({
  selector: 'app-storage-table',
  templateUrl: './storage-table.component.html',
  styleUrls: ['./storage-table.component.scss']
})
export class StorageTableComponent implements OnInit {

  pageSize        = 10;
  pageIndex       = 0;

  pageSizeOptions = [5,10,20];

  displayedColumns: string[] = [
    'name', 'capacity', 'fullness', 'update', 'additionalInfo', 'delete'
  ];

  @Input() dataSource!: MatTableDataSource<Storage>;
  @Input() pageRequest!: PageRequestStorage;
  @Input() length!: number;

  @Output() updateEvent: EventEmitter<Storage> = new EventEmitter();
  @Output() deleteEvent: EventEmitter<Storage> = new EventEmitter();
  @Output() cropsInfo  : EventEmitter<Storage> = new EventEmitter();
  @Output() page  : EventEmitter<any>   = new EventEmitter();

  constructor() {
  }

  ngOnInit(): void {
    console.log(this.dataSource.data)
  }

  update(storage: Storage) {
    this.updateEvent.emit(storage);
  }

  delete(storage : Storage) {
    this.deleteEvent.emit(storage);
  }

  pageEvent(page: PageEvent) {
    this.pageIndex = page.pageIndex;
    this.pageSize  = page.pageSize;

    this.pageRequest.page     = this.pageIndex;
    this.pageRequest.pageSize = this.pageSize;

    this.page.emit();
  }

  info(element: Storage) {
    this.cropsInfo.emit(element);
  }


}
