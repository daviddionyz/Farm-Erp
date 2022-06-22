import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DialogService} from "./dialog.service";
import {EMPTY, Observable} from "rxjs";
import {BaseResponseDTO} from "../models/base-response-dto";
import {environment} from "../../environments/environment";
import {catchError, finalize} from "rxjs/operators";
import {Storage} from "../models/storage/storage";
import {StorageUpdateDialogComponent} from "../dialogs/storage-update-dialog/storage-update-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {PageRequestStorage} from "../models/page-requests/page-request-storage";
import {StorageCropsDialogComponent} from "../dialogs/storage-crops-dialog/storage-crops-dialog.component";
import {Crops} from "../models/storage/Crops";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor(
    private http : HttpClient,
    private dialogService : DialogService,
    private dialog : MatDialog
  ) {}


  getALlStorage(pageRequest: PageRequestStorage) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    let url = `${environment.url}/storage/all?page=${pageRequest.page}&pageSize=${pageRequest.pageSize}`

    if (pageRequest.name !== '' && pageRequest.name !== null && pageRequest.name ){
      url = url.concat(`&name=${pageRequest.name}`)
    }

    return this.http.get<BaseResponseDTO>( url).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  getALlStorageWithoutPageRequest() : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.get<BaseResponseDTO>( `${environment.url}/storage/all/every`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  getCropsInfo(storageId: number){
    this.dialogService.showSpinner();

    return this.http.get<BaseResponseDTO>( `${environment.url}/storage/get/crops?storageId=${storageId}`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  deleteStorage(storageId: number) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.delete<BaseResponseDTO>(`${environment.url}/storage/delete?storageId=${storageId}`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  createNewStorage(storageDto: Storage) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.post<BaseResponseDTO>(`${environment.url}/storage/add`, storageDto ).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  updateStorage(storageDto: Storage) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.put<BaseResponseDTO>(`${environment.url}/storage/update`, storageDto ).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  openUpdateDialog(storage: Storage){
    const dialogRef = this.dialog.open(StorageUpdateDialogComponent, {
      width: '300px',
      data: storage
    });

    return dialogRef.afterClosed();
  }

  openCropsDialog(crops: Crops[]){
    const dialogRef = this.dialog.open(StorageCropsDialogComponent, {
      width: '500px',
      data: crops
    });

    return dialogRef.afterClosed();
  }

}
