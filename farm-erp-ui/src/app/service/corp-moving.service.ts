import { Injectable } from '@angular/core';
import {DeliveriesService} from "./deliveries.service";
import {environment} from "../../environments/environment";
import {BaseResponseDTO} from "../models/base-response-dto";
import {catchError, finalize} from "rxjs/operators";
import {EMPTY} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {DialogService} from "./dialog.service";
import {MatDialog} from "@angular/material/dialog";

@Injectable({
  providedIn: 'root'
})
export class CorpMovingService {

  constructor(
    private http         : HttpClient,
    private dialogService: DialogService,
    private dialog       : MatDialog,
  ) { }

  getDeliveriesCorpMoving(searchCriteria: string, page: number, pageSize: number){
    this.dialogService.showSpinner();

    console.log(searchCriteria)

    let url = `${environment.url}/corpmoving/all?page=${page}&pageSize=${pageSize}`

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
}
