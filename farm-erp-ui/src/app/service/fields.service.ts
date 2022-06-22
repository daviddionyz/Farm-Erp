import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {catchError, finalize} from "rxjs/operators";
import {EMPTY, Observable} from "rxjs";
import {DialogService} from "./dialog.service";
import {environment} from "../../environments/environment";
import { Field } from "../models/fields/field";
import {BaseResponseDTO} from "../models/base-response-dto";
import {Storage} from "../models/storage/storage";
import {StorageUpdateDialogComponent} from "../dialogs/storage-update-dialog/storage-update-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {FieldsUpdateDialogComponent} from "../dialogs/fields-update-dialog/fields-update-dialog.component";
import {PageRequestFields} from "../models/page-requests/page-request-fields";

@Injectable({
  providedIn: 'root'
})
export class FieldsService {

  constructor(
    private http : HttpClient,
    private dialogService : DialogService,
    private dialog : MatDialog,
  ) {}


  getALlField(pageRequest: PageRequestFields) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    let url = `${environment.url}/field/all?page=${pageRequest.page}&pageSize=${pageRequest.pageSize}`

    if (pageRequest.name !== '' && pageRequest.name !== null && pageRequest.name ){
      url = url.concat(`&name=${pageRequest.name}`)
    }

    if (pageRequest.corpType !== '' && pageRequest.corpType !== null && pageRequest.corpType ){
      url = url.concat(`&corpType=${pageRequest.corpType}`)
    }

    if (pageRequest.corpName !== '' && pageRequest.corpName !== null && pageRequest.corpName ){
      url = url.concat(`&corpName=${pageRequest.corpName}`)
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

  getALlFieldWithoutPageRequest() : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.get<BaseResponseDTO>( `${environment.url}/field/all/every`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  deleteField(fieldId: number) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.delete<BaseResponseDTO>(`${environment.url}/field/delete?fieldId=${fieldId}`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  createNewField(field: Field) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.post<BaseResponseDTO>(`${environment.url}/field/add`, field ).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  updateField(field: Field) : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.put<BaseResponseDTO>(`${environment.url}/field/update`, field ).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  openUpdateDialog(fieldDTO: Field){
    const dialogRef = this.dialog.open(FieldsUpdateDialogComponent, {
      width: '300px',
      data: fieldDTO
    });

    return dialogRef.afterClosed();
  }
}
