import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Deliveries} from "../../../../models/harvest/Deliveries";
import {StorageService} from "../../../../service/storage.service";
import {VehiclesService} from "../../../../service/vehicles.service";
import {WorkersService} from "../../../../service/workers.service";
import {FieldsService} from "../../../../service/fields.service";
import {Worker} from "../../../../models/worker/worker";
import {Field} from "../../../../models/fields/field";
import {Vehicles} from "../../../../models/vehicles/vehicles";
import {Storage} from "../../../../models/storage/storage";
import {Crops} from "../../../../models/storage/Crops";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-delivery-create',
  templateUrl: './delivery-create-dialog.component.html',
  styleUrls: ['./delivery-create-dialog.component.scss']
})
export class DeliveryCreateDialogComponent implements OnInit {

  workers: Worker[] = [];
  fields: Field[] = [];
  vehicles: Vehicles[] = [];
  storages: Storage[] = [];

  crops: Crops[] = [];
  selectedCrop: any | null = null;
  froms: any[] = [];

  newCrop: Crops = {
    amount: 0,
    cropName: "",
    cropType: ""
  };

  constructor(
    public dialogRef: MatDialogRef<DeliveryCreateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Deliveries,
    private storageService: StorageService,
    private vehicleService: VehiclesService,
    private workerService: WorkersService,
    private fieldService: FieldsService,
  ) {}

  ngOnInit(): void {
    console.log(this.data)

    this.workerService.getALlWorkerWithoutPageRequest().subscribe(
      res => {
        this.workers = res.data;
      }
    )

    this.vehicleService.getALlVehicleWithoutPageRequest().subscribe(
      res => {
        this.vehicles = res.data;
      }
    )

    this.fieldService.getALlFieldWithoutPageRequest().subscribe(
      res => {
        this.fields = res.data;
      }
    )

    this.storageService.getALlStorageWithoutPageRequest().subscribe(
      res => {
        this.storages = res.data;

        this.storages.push({
          'id': null,
          'name': 'Külső helyszín',
          capacity: 0, fullness: 0
        })

        this.storages.forEach(storage => {
          if (storage.fullness > 0 || storage.id === null)
            this.froms.push(storage);
        })
      }
    )

  }

  onNoClick() {
    this.dialogRef.close();
  }

  onOkClick() {

    this.data.worker = this.findElementInArray(this.workers, this.data.worker, true);
    this.data.vehicle = this.findElementInArray(this.vehicles, this.data.vehicle, true);

    this.data.from = this.findElementInArray(this.fields, this.data.from, false);
    this.data.fromStorage = this.findElementInArray(this.storages, this.data.fromStorage, false);
    this.data.where = this.findElementInArray(this.storages, this.data.where, false);

    if (this.newCrop.cropName !== '' && this.newCrop.cropType !== ''){
      this.newCrop.amount = this.data.net;
      this.data.crop = this.newCrop;
    }else if (this.data.from) {
      console.log(this.data.from);
      this.data.crop = {
        amount: this.data.net,
        cropName: this.data.from.corpName,
        cropType: this.data.from.corpType
      };
    }

    console.log(this.data);
    this.dialogRef.close(this.data);
  }

  findElementInArray(objectList: any[], object: any, isId: boolean): any {
    let result: any = null

    if (object === null) {
      return result;
    }

    if (!isId) {
      objectList.forEach(member => {
        if (member.name === object) {
          result = member;
        }
      })
    } else {
      objectList.forEach(member => {
        if (Number(member.id) === Number(object)) {
          result = member;
        }
      });
    }

    return result;
  }

  fromChanged($event: MatSelectChange) {
    let storage: Storage = this.findElementInArray(this.storages, this.data.fromStorage, false);
    this.data.crop === null;

    if (storage !== null) {
      console.log(storage);
      this.storageService.getCropsInfo(storage?.id ?? -1).subscribe(
        data => {
          this.crops = data.data;
        });
    }
  }

  isCropSelected() {
    if (this.data.isCorpMoving){
      // @ts-ignore
      if (this.data.fromStorage !== 'Külső helyszín'){
        return (this.data.crop === undefined )
      } else {
        return (this.newCrop.cropName === '' || this.newCrop.cropType === '');
      }
    } else {
      return false;
    }
  }

  calcNet() {
    if (this.data.gross) {
      if (this.data.empty) {
        this.data.net = this.data.gross - this.data.empty;
      } else {
        this.data.net = this.data.gross;
      }
    }
  }

  workerSelected() {
    let worker = this.workers[this.workers.findIndex(wr => wr.id === Number(this.data.worker))]
    if (worker) {
      // @ts-ignore
      this.data.vehicle = worker.vehicle?.id;
    }
  }

  isBalanceCorrect() {
    return this.data.gross > 0 && this.data.empty > 0 && this.data.gross - this.data.empty >= 0;
  }
}
