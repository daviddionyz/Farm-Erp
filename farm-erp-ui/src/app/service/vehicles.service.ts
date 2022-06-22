import { Injectable } from '@angular/core';
import {EMPTY, Observable} from "rxjs";
import {BaseResponseDTO} from "../models/base-response-dto";
import {environment} from "../../environments/environment";
import {catchError, finalize} from "rxjs/operators";
import {Storage} from "../models/storage/storage";
import {HttpClient} from "@angular/common/http";
import {DialogService} from "./dialog.service";
import {MatDialog} from "@angular/material/dialog";
import {Vehicles} from "../models/vehicles/vehicles";
import {StorageUpdateDialogComponent} from "../dialogs/storage-update-dialog/storage-update-dialog.component";
import {VehiclesUpdateDialogComponent} from "../dialogs/vehicles-update-dialog/vehicles-update-dialog.component";
import {PageRequestVehicles} from "../models/page-requests/page-request-vehicles";

@Injectable({
  providedIn: 'root'
})
export class VehiclesService {

  constructor(
    private http : HttpClient,
    private dialogService : DialogService,
    private dialog : MatDialog
  ) { }

  getALlVehicles(pageRequest: PageRequestVehicles) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    let url = `${environment.url}/vehicles/all?page=${pageRequest.page}&pageSize=${pageRequest.pageSize}`

    if (pageRequest.name !== '' && pageRequest.name !== null && pageRequest.name ){
      url = url.concat(`&name=${pageRequest.name}`)
    }

    if (pageRequest.type !== '' && pageRequest.type !== null && pageRequest.type ){
      url = url.concat(`&type=${pageRequest.type}`)
    }

    if (Number.isInteger(pageRequest.status) ){
      url = url.concat(`&status=${pageRequest.status}`)
    }


    return this.http.get<BaseResponseDTO>(url ).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  getALlVehicleWithoutPageRequest() : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.get<BaseResponseDTO>( `${environment.url}/vehicles/all/every`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  deleteVehicle(vehicleId: number) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.delete<BaseResponseDTO>(`${environment.url}/vehicles/delete?vehicleId=${vehicleId}`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  createNewVehicle(vehicles: Vehicles) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.post<BaseResponseDTO>(`${environment.url}/vehicles/add`, vehicles ).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  updateVehicle(vehicles: Vehicles) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.put<BaseResponseDTO>(`${environment.url}/vehicles/update`, vehicles ).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  openUpdateDialog(vehicles: Vehicles){
    const dialogRef = this.dialog.open(VehiclesUpdateDialogComponent, {
      width: '300px',
      data: vehicles
    });

    return dialogRef.afterClosed();
  }
}
