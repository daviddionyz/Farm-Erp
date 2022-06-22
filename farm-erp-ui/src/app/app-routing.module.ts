import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomeComponent} from "./components/welcome/welcome.component";
import {LoginComponent} from "./components/auth/login/login.component";
import {FieldsComponent} from "./components/fields/fields.component";
import {WorkersComponent} from "./components/workers/workers.component";
import {VehiclesComponent} from "./components/vehicles/vehicles.component";
import {StorageComponent} from "./components/storage/storage.component";
import {AuthGuard} from "./components/auth/auth.guard";
import {HarvestComponent} from "./components/harvest/harvest.component";
import {CorpMovingComponent} from "./components/corp-moving/corp-moving.component";

const routes: Routes = [
  {
    component: LoginComponent,
    path: '',
  },
  {
    component: FieldsComponent,
    canActivate : [AuthGuard],
    path: 'fields',
  },
  {
    component: WorkersComponent,
    canActivate : [AuthGuard],
    path: 'workers',
  },
  {
    component: VehiclesComponent,
    canActivate : [AuthGuard],
    path: 'vehicles',
  },
  {
    component: StorageComponent,
    canActivate : [AuthGuard],
    path: 'storages',
  },
  {
    component: WelcomeComponent,
    canActivate : [AuthGuard],
    path: 'welcome',
  },
  {
    component: HarvestComponent,
    canActivate : [AuthGuard],
    path: 'harvest',
  },
  {
    component: CorpMovingComponent,
    canActivate : [AuthGuard],
    path: 'corpmoving',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
