import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DialogService} from "./dialog.service";
import {MatDialog} from "@angular/material/dialog";
import {BaseResponseDTO} from "../models/base-response-dto";
import {environment} from "../../environments/environment";
import {catchError, finalize} from "rxjs/operators";
import {EMPTY} from "rxjs";
import {DeliveryCreateDialogComponent} from "../dialogs/delivery-create-dialog/delivery-create-dialog.component";
import {Deliveries} from "../models/harvest/Deliveries";
import {DatePipe} from "@angular/common";

@Injectable({
  providedIn: 'root'
})
export class DeliveriesService {

  constructor(
    private http         : HttpClient,
    private dialogService: DialogService,
    private dialog       : MatDialog,
  ) { }


  getDeliveriesByDiary(diaryId: number, searchCriteria: string, page: number, pageSize: number){
    this.dialogService.showSpinner();

    console.log(searchCriteria)

    let url = `${environment.url}/deliveries/get/${diaryId}?page=${page}&pageSize=${pageSize}`

    if(searchCriteria && searchCriteria !== '') {
      url = url.concat(`&search=${searchCriteria}`);
    }

    return this.http.get<BaseResponseDTO>(url).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  updateDelivery(delivery: Deliveries){
    this.dialogService.showSpinner();

    return this.http.put<BaseResponseDTO>(`${environment.url}/deliveries/update`,delivery).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  createNewDelivery(delivery: Deliveries){
    this.dialogService.showSpinner();

    return this.http.post<BaseResponseDTO>(`${environment.url}/deliveries/create`,delivery).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  deleteDelivery(deliveryId: number){
    this.dialogService.showSpinner();

    return this.http.delete<BaseResponseDTO>(`${environment.url}/deliveries/delete?deliveryId=${deliveryId}`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  openCreateDialog(diaryId: number | null, isCorpMoving? : boolean, delivery? : Deliveries){
    const datepipe: DatePipe = new DatePipe('en-US');

    console.log(delivery)

    const dialogRef = this.dialog.open(DeliveryCreateDialogComponent, {
      width: '300px',
      disableClose: true,
      data: delivery?
        {
          'id'           : delivery.id,
          'diaryId'      : diaryId,
          'worker'       : delivery.worker?.id ?? null,
          'vehicle'      : delivery.vehicle?.id ?? null,
          'gross'        : delivery.gross,
          'empty'        : delivery.empty,
          'net'          : delivery.net,
          'intakeDate'   : String(datepipe.transform(delivery.intakeDate, 'yyyy-MM-ddTHH:mm:ss')),
          'from'         : delivery.from?.name ?? delivery.fromStorage?.name ?? 'Külső helyszín',
          'where'        : delivery.where?.name ?? 'Külső helyszín',
          'isCorpMoving' : isCorpMoving ?? false
        }
        :
        {
          'diaryId' : diaryId,
          'isCorpMoving' : isCorpMoving ?? false
        }
    });

    return dialogRef.afterClosed();

  }
}
