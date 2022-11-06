import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {createDependencyInfo} from "@angular/compiler-cli/ngcc/src/dependencies/dependency_host";
import {environment} from "../../environments/environment";
import {BaseResponseDTO} from "../models/base-response-dto";
import {catchError, finalize} from "rxjs/operators";
import {EMPTY} from "rxjs";
import {DialogService} from "./dialog.service";
import {Diary} from "../models/harvest/Diary";
import {Storage} from "../models/storage/storage";
import {StorageUpdateDialogComponent} from "../components/storage/storage-update-dialog/storage-update-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {HarvestDiaryCreateDialogComponent} from "../components/harvest/harvest-diary-create-dialog/harvest-diary-create-dialog.component";

@Injectable({
  providedIn: 'root'
})
export class HarvestService {

  constructor(
    private http         : HttpClient,
    private dialogService: DialogService,
    private dialog       : MatDialog,
  ) { }

  getAllHarvestDiaryYear(){
    this.dialogService.showSpinner();

    return this.http.get<BaseResponseDTO>(`${environment.url}/harvest/all`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  createHarvestDiary(newDiary: Diary){
    this.dialogService.showSpinner();

    return this.http.post<BaseResponseDTO>(`${environment.url}/harvest/create`,newDiary).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  deleteHarvestDiary(diaryId: number){
    this.dialogService.showSpinner();

    return this.http.delete<BaseResponseDTO>(`${environment.url}/harvest/delete?diaryId=${diaryId}`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  openCreateDialog(){
    const dialogRef = this.dialog.open(HarvestDiaryCreateDialogComponent, {
      width: '300px',
      data: {}
    });

    return dialogRef.afterClosed();

  }
}

