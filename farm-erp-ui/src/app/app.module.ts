import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";
import {BrowserModule} from '@angular/platform-browser';
import {MaterialModule} from "./material.module";

import {AppComponent} from './app.component';
import {LoginComponent} from './components/auth/login/login.component';
import {WelcomeComponent} from './components/welcome/welcome.component';
import {StorageComponent} from './components/storage/storage.component';
import {WorkersComponent} from './components/workers/workers.component';
import {VehiclesComponent} from './components/vehicles/vehicles.component';
import {FieldsComponent} from './components/fields/fields.component';
import {HarvestComponent} from './components/harvest/harvest.component';
import {CorpMovingComponent} from './components/corp-moving/corp-moving.component';
import {SpinnerComponent} from "./shared/spinner/spinner.component";
import {DialogComponent} from "./shared/dialog/dialog.component";
import {ValidationDialogComponent} from "./shared/validation-dialog/validation-dialog.component";
import {StorageUpdateDialogComponent} from "./dialogs/storage-update-dialog/storage-update-dialog.component";
import {StorageTableComponent} from './components/storage/storage-table/storage-table.component';
import {StorageCreateSearchComponent} from './components/storage/storage-create-search/storage-create-search.component';
import {FieldTableComponent} from './components/fields/field-table/field-table.component';
import {FieldCreateSearchComponent} from './components/fields/field-create-search/field-create-search.component';
import {VehiclesTableComponent} from './components/vehicles/vehicles-table/vehicles-table.component';
import {VehiclesCreateSearchComponent} from './components/vehicles/vehicles-create-search/vehicles-create-search.component';
import {WorkersTableComponent} from './components/workers/workers-table/workers-table.component';
import {WorkersCreateSearchComponent} from './components/workers/workers-create-search/workers-create-search.component';
import {FieldsUpdateDialogComponent} from './dialogs/fields-update-dialog/fields-update-dialog.component';
import {WorkersUpdateDialogComponent} from './dialogs/workers-update-dialog/workers-update-dialog.component';
import {VehiclesUpdateDialogComponent} from './dialogs/vehicles-update-dialog/vehicles-update-dialog.component';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {JwtInterceptor} from "./interceptors/jwt.interceptor";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {ReactiveFormsModule} from "@angular/forms";
import {DeliveriesTableComponent} from './components/harvest/deliveries-table/deliveries-table.component';
import {HarvestDiaryCreateDialogComponent} from './dialogs/harvest-diary-create-dialog/harvest-diary-create-dialog.component';
import {DeliveryCreateDialogComponent} from './dialogs/delivery-create-dialog/delivery-create-dialog.component';
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {StorageCropsDialogComponent} from './dialogs/storage-crops-dialog/storage-crops-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    WelcomeComponent,
    StorageComponent,
    WorkersComponent,
    VehiclesComponent,
    FieldsComponent,
    HarvestComponent,
    CorpMovingComponent,
    SpinnerComponent,
    DialogComponent,
    SpinnerComponent,
    ValidationDialogComponent,
    StorageUpdateDialogComponent,
    StorageTableComponent,
    StorageCreateSearchComponent,
    FieldTableComponent,
    FieldCreateSearchComponent,
    VehiclesTableComponent,
    VehiclesCreateSearchComponent,
    WorkersTableComponent,
    WorkersCreateSearchComponent,
    FieldsUpdateDialogComponent,
    WorkersUpdateDialogComponent,
    VehiclesUpdateDialogComponent,
    DeliveriesTableComponent,
    HarvestDiaryCreateDialogComponent,
    DeliveryCreateDialogComponent,
    StorageCropsDialogComponent,
  ],
  imports: [
    MaterialModule,
    BrowserModule,
    CommonModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    NoopAnimationsModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
  ],
  bootstrap: [AppComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
})
export class AppModule {
}
