import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Storage} from "../../../models/storage/storage";
import {PageRequestStorage} from "../../../models/page-requests/page-request-storage";

@Component({
  selector: 'app-create-storage',
  templateUrl: './storage-create-search.component.html',
  styleUrls: ['./storage-create-search.component.scss']
})
export class StorageCreateSearchComponent implements OnInit {

  isCreate = false;

  newStorage : Storage = {
    id: 0,
    name : '',
    capacity: 0,
    fullness: 0,
  }

  okButtonText: string = "asd";

  @Input() mode!: string;

  @Output() submit: EventEmitter<Storage> = new EventEmitter<Storage>();
  @Output() clear: EventEmitter<any> = new EventEmitter <any>();
  constructor() {

  }

  ngOnInit(): void {
    this.textInit();
  }

  textInit(){
    this.isCreate = this.mode === 'create';

    switch (this.mode) {
      case 'search':
        this.okButtonText = 'Keresés'
        break;
      case 'create':
        this.okButtonText = 'Létrehozás'
        break;
    }
  }

  onClear() {
    this.newStorage.name     = '';
    this.newStorage.capacity = 0;
    this.newStorage.fullness = 0;

    this.clear.emit();
  }

  onSubmit() {
    this.submit.emit(this.newStorage);
    if (this.mode === 'create'){
      this.newStorage.name     = '';
      this.newStorage.capacity = 0;
      this.newStorage.fullness = 0;
    }

  }

  checkIdfDataIsGiven() {
    switch (this.mode) {
      case 'create':
        return !(this.newStorage.name !== ''
          && Number.isInteger(this.newStorage.capacity)
          && Number.isInteger(this.newStorage.fullness)
          && this.newStorage.capacity > 0)
      default:
        return false;
    }
  }
}
