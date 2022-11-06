import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Worker} from "../../../models/worker/worker";
import {FormGroup, FormControl} from "@angular/forms";
@Component({
  selector: 'app-workers-create-search',
  templateUrl: './workers-create-search.component.html',
  styleUrls: ['./workers-create-search.component.scss']
})
export class WorkersCreateSearchComponent implements OnInit {

  isCreate = false;
  datepipe: DatePipe = new DatePipe('en-US');

  newWorker: Worker = {
    id: 0,
    name: '',
    joinDate: '',
    vehicle: null,
    position: ''
  }

  createdAtDateFrom = new FormGroup(
    {
      start: new FormControl(),
      end: new FormControl()
    }
  )

  okButtonText : string     = "";
  vehicles     : Vehicles[] = [];

  @Input() mode!: string;
  @Input() pageRequest!: PageRequestWorkers;

  @Output() submit: EventEmitter<Worker> = new EventEmitter<Worker>();
  @Output() clear: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private vehicleService: VehiclesService
  ) {
    vehicleService.getALlVehicleWithoutPageRequest().subscribe(
      vehicles => {
        this.vehicles = vehicles.data;
      }
    )
  }

  ngOnInit(): void {
    this.textInit();
  }

  textInit() {
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
    this.newWorker.name = '';
    this.newWorker.joinDate = '';
    this.newWorker.vehicle = null;
    this.newWorker.position = '';

    this.createdAtDateFrom = new FormGroup({
      start: new FormControl(),
      end: new FormControl(),
    })

    this.pageRequest.createdAtFrom = String(this.datepipe.transform(this.createdAtDateFrom.value.start, 'yyyy.MM.dd.') ?? '');
    this.pageRequest.createdAtTo   = String(this.datepipe.transform(this.createdAtDateFrom.value.end  , 'yyyy.MM.dd.') ?? '');
    console.log(this.createdAtDateFrom);
    this.clear.emit();
  }

  onSubmit() {
    if (this.mode === 'search') {
      console.log(this.createdAtDateFrom);

      this.pageRequest.createdAtFrom = String(this.datepipe.transform(this.createdAtDateFrom.value.start, 'yyyy.MM.dd.') ?? '');
      this.pageRequest.createdAtTo   = String(this.datepipe.transform(this.createdAtDateFrom.value.end  , 'yyyy.MM.dd.') ?? '');
    }

    this.submit.emit(this.newWorker);
  }


  checkIdfDataIsGiven() {
    switch (this.mode) {
      case 'create':
        return !(this.newWorker.name !== ''
          && this.newWorker.position !== ''
          && this.newWorker.joinDate !== ''
        )
      default:
        return false;
    }
  }
}
import {PageRequestWorkers} from "../../../models/page-requests/page-request-workers";
import {DatePipe} from "@angular/common";
import {VehiclesService} from "../../../service/vehicles.service";


import {Vehicles} from "../../../models/vehicles/vehicles";
