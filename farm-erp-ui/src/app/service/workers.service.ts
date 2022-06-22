import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {catchError, finalize} from "rxjs/operators";
import {EMPTY, Observable} from "rxjs";
import {DialogService} from "./dialog.service";
import {MatDialog} from "@angular/material/dialog";
import {BaseResponseDTO} from "../models/base-response-dto";
import {Worker} from "../models/worker/worker";
import {WorkersUpdateDialogComponent} from "../dialogs/workers-update-dialog/workers-update-dialog.component";
import {PageRequestWorkers} from "../models/page-requests/page-request-workers";
import {DatePipe} from "@angular/common";

@Injectable({
  providedIn: 'root'
})
export class WorkersService {

  constructor(
    private http: HttpClient,
    private dialogService: DialogService,
    private dialog: MatDialog
  ) {
  }

  getALlWorkers(pageRequest: PageRequestWorkers): Observable<BaseResponseDTO> {
    this.dialogService.showSpinner();

    let url = `${environment.url}/worker/all?page=${pageRequest.page}&pageSize=${pageRequest.pageSize}`

    if (pageRequest.name !== '' && pageRequest.name !== null && pageRequest.name ){
      url = url.concat(`&name=${pageRequest.name}`)
    }

    if (pageRequest.position !== '' && pageRequest.position !== null && pageRequest.position ){
      url = url.concat(`&position=${pageRequest.position}`)
    }

    if (Number.isInteger(pageRequest.vehicle ) ){
      url = url.concat(`&vehicle=${pageRequest.vehicle}`)
    }

    if (pageRequest.createdAtFrom !== '' && pageRequest.createdAtFrom !== 'null' ) {
      url = url.concat(`&createdAtFrom=${pageRequest.createdAtFrom}`);
    }

    if (pageRequest.createdAtTo !== '' && pageRequest.createdAtTo !== 'null') {
      url = url.concat(`&createdAtTo=${pageRequest.createdAtTo}`);
    }

    return this.http.get<BaseResponseDTO>(url).pipe(
      finalize(() => this.dialogService.stopSpinner()),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  getALlWorkerWithoutPageRequest() : Observable<BaseResponseDTO>{
    this.dialogService.showSpinner();

    return this.http.get<BaseResponseDTO>( `${environment.url}/worker/all/every`).pipe(
      finalize(() => this.dialogService.stopSpinner() ),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );
  }

  deleteWorker(workerId: number): Observable<BaseResponseDTO> {
    this.dialogService.showSpinner();

    return this.http.delete<BaseResponseDTO>(`${environment.url}/worker/delete?workerId=${workerId}`).pipe(
      finalize(() => this.dialogService.stopSpinner()),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  createNewWorker(worker: Worker): Observable<BaseResponseDTO> {
    console.log(worker)
    this.dialogService.showSpinner();

    // worker.joinDate = worker.joinDate.concat(" 00:00:00")

    return this.http.post<BaseResponseDTO>(`${environment.url}/worker/add`, worker).pipe(
      finalize(() => this.dialogService.stopSpinner()),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  updateWorker(worker: Worker): Observable<BaseResponseDTO> {
    this.dialogService.showSpinner();

    return this.http.put<BaseResponseDTO>(`${environment.url}/worker/update`, worker).pipe(
      finalize(() => this.dialogService.stopSpinner()),
      catchError(errors => {
        console.log(errors);
        this.dialogService.stopSpinner();
        this.dialogService.openDialog('Váratlan hiba történt', 'info');
        return EMPTY;
      })
    );

  }

  openUpdateDialog(worker: Worker) {
    const datepipe: DatePipe = new DatePipe('en-US');

    const dialogRef = this.dialog.open(WorkersUpdateDialogComponent, {
      width: '300px',
      data: {
        'id'        : worker.id,
        'name'      : worker.name,
        'vehicle'   : worker.vehicle?.id ?? -1,
        'position'  : worker.position,
        'joinDate'  : String(datepipe.transform(worker.joinDate, 'yyyy-MM-dd')),
      }
    });

    return dialogRef.afterClosed();
  }
}
