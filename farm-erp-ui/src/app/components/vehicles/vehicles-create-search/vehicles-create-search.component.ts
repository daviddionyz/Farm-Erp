import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Field} from "../../../models/fields/field";
import {Vehicles} from "../../../models/vehicles/vehicles";

@Component({
  selector: 'app-vehicles-create-search',
  templateUrl: './vehicles-create-search.component.html',
  styleUrls: ['./vehicles-create-search.component.scss']
})
export class VehiclesCreateSearchComponent implements OnInit {

  isCreate = false;

  newVehicle : Vehicles = {
    id: 0,
    name : '',
    type: '',
    status : Number.NaN,
  }

  okButtonText: string = "";

  @Input() mode!: string;

  @Output() submit: EventEmitter<Vehicles> = new EventEmitter<Vehicles>();
  @Output() clear: EventEmitter<any> = new EventEmitter<any>();

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
    this.newVehicle.name     = '';
    this.newVehicle.type     = '';
    this.newVehicle.status   = Number.NaN;

    this.clear.emit();
  }

  onSubmit() {
    this.submit.emit(this.newVehicle);
    if (this.mode === 'create'){
      this.newVehicle.name     = '';
      this.newVehicle.type     = '';
      this.newVehicle.status   = Number.NaN;
    }
  }

  checkIdfDataIsGiven() {
    switch (this.mode) {
      case 'create':
        return !(this.newVehicle.name !== '' && this.newVehicle.type !== '' && Number.isInteger(this.newVehicle.status))
      default:
        return false;
    }
  }
}
