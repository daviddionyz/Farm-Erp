import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Deliveries} from "../../models/harvest/Deliveries";
import {StorageService} from "../../service/storage.service";
import {VehiclesService} from "../../service/vehicles.service";
import {WorkersService} from "../../service/workers.service";
import {FieldsService} from "../../service/fields.service";
import {Worker} from "../../models/worker/worker";
import {Field} from "../../models/fields/field";
import {Vehicles} from "../../models/vehicles/vehicles";
import {Storage} from "../../models/storage/storage";
import {Crops} from "../../models/storage/Crops";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-delivery-create',
  templateUrl: './delivery-create-dialog.component.html',
  styleUrls: ['./delivery-create-dialog.component.scss']
})
export class DeliveryCreateDialogComponent implements OnInit {

  workers        : Worker[]   = [];
  fields         : Field[]    = [];
  vehicles       : Vehicles[] = [];
  storages       : Storage[]  = [];

  isWhereStorage : boolean    = false;
  crops          : Crops[]    = [];
  selectedCrop   : any | null = null;
  froms          : any[]      = [];

  constructor(
    public dialogRef: MatDialogRef<DeliveryCreateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Deliveries,
    private storageService: StorageService,
    private vehicleService: VehiclesService,
    private workerService : WorkersService,
    private fieldService  : FieldsService,
  ) {
  }

  ngOnInit(): void {
    console.log(this.data)

    this.workerService.getALlWorkerWithoutPageRequest().subscribe(
      data => {
        this.workers = data.data;
      }
    )

    this.vehicleService.getALlVehicleWithoutPageRequest().subscribe(
      data => {
        this.vehicles = data.data;
      }
    )

    this.fieldService.getALlFieldWithoutPageRequest().subscribe(
      data => {
        this.fields = data.data;

        this.fields.forEach( member => {
          this.froms.push(member)
        })

        this.fields.push({
          'id'   : null,
          'name' : 'Külső helyszín',
          corpType: "", size: 0,
          corpName: "",
        })

        console.log(this.froms)
      }
    )

    this.storageService.getALlStorageWithoutPageRequest().subscribe(
      data => {
        this.storages = data.data;

        this.storages.push({
          'id'   : null,
          'name' : 'Külső helyszín',
          capacity: 0, fullness: 0
        })

        this.storages.forEach( member => {
          this.froms.push(member)
        })
      }
    )

  }

  onNoClick() {
    this.dialogRef.close();
  }

  onOkClick(){

    this.data.worker      = this.findElementInArray(this.workers,this.data.worker,true);
    this.data.vehicle     = this.findElementInArray(this.vehicles,this.data.vehicle,true);

    let fromName = this.data.from;

    this.data.from        = this.findElementInArray(this.fields,fromName,false);
    this.data.fromStorage = this.findElementInArray(this.storages,fromName, false);
    this.data.where       = this.findElementInArray(this.storages,this.data.where, false);

    console.log(this.data);
    this.dialogRef.close(this.data);
  }

  findElementInArray(objectList: any[], object: any , isId: boolean) : any{
    let result : any = null

    console.log("object")
    console.log(object)

    if (object === null || object  === 'Külső helyszín'){
      return result;
    }

    if (!isId){
      objectList.forEach( member => {
        if (member.name === object){
          console.log(member)
          result =  member;
        }
      })
    }else{
      objectList.forEach( member => {
        if ( Number(member.id) === Number(object)){
          result =  member;
        }
      })
    }

    return result;
  }

  fromChanged($event: MatSelectChange) {
    let storage : Storage = this.findElementInArray(this.storages, this.data.from, false);
    console.log("asd")

    if (storage !== null){
      this.isWhereStorage = true;
      this.storageService.getCropsInfo(storage?.id ?? -1).subscribe(
        data => {
          if (data.data){
            this.crops = data.data;
          }
        }
      )
    }else{
      this.isWhereStorage = false;
    }
  }

  isCropSelected() {
    return (this.isWhereStorage && this.data.crop === undefined);
  }

  calcNet(){
    if (this.data.gross){
      if (this.data.empty){
        this.data.net = this.data.gross - this.data.empty;
      }else{
        this.data.net = this.data.gross;
      }
    }
  }
}
