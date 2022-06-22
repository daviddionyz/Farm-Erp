import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { MatSidenavModule} from "@angular/material/sidenav";
import { MatDialogModule} from "@angular/material/dialog";
import { MatToolbarModule} from "@angular/material/toolbar";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxSpinnerModule } from 'ngx-spinner';
import { MatButtonModule} from "@angular/material/button";
import { MatIconModule} from "@angular/material/icon";
import { FlexModule} from "@angular/flex-layout";
import { MatInputModule} from "@angular/material/input";
import { HttpClientModule} from "@angular/common/http";
import { MatButtonToggleModule} from "@angular/material/button-toggle";
import { DragDropModule} from "@angular/cdk/drag-drop";
import { MatTableModule} from "@angular/material/table";
import { MatFormFieldModule} from "@angular/material/form-field";
import { MatSelectModule} from "@angular/material/select";
import { FormsModule} from "@angular/forms";
import { MatRadioModule} from "@angular/material/radio";
import { MatListModule} from "@angular/material/list";
import { MatGridListModule} from "@angular/material/grid-list";
import { MatMenuModule} from "@angular/material/menu";
import { MatPaginatorModule} from "@angular/material/paginator";
import { MatSortModule} from "@angular/material/sort";
import { MatCardModule} from "@angular/material/card";
import { ScrollingModule} from "@angular/cdk/scrolling";
import { MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatNativeDateModule} from "@angular/material/core";

@NgModule({
  exports: [
    AppRoutingModule,
    MatSidenavModule,
    MatDialogModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    BrowserAnimationsModule,
    FlexModule,
    NgxSpinnerModule,
    MatInputModule,
    HttpClientModule,
    MatButtonToggleModule,
    DragDropModule,
    MatTableModule,
    MatFormFieldModule,
    MatSelectModule,
    MatRadioModule,
    MatListModule,
    MatGridListModule,
    MatMenuModule,
    MatPaginatorModule,
    MatSortModule,
    MatCardModule,
    ScrollingModule,
    MatSlideToggleModule,
    FormsModule,
    MatNativeDateModule,
  ],
  imports: [
    AppRoutingModule,
    MatSidenavModule,
    MatDialogModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    BrowserAnimationsModule,
    FlexModule,
    NgxSpinnerModule,
    MatInputModule,
    HttpClientModule,
    MatButtonToggleModule,
    DragDropModule,
    MatTableModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    MatRadioModule,
    MatListModule,
    MatGridListModule,
    MatMenuModule,
    MatPaginatorModule,
    MatSortModule,
    MatCardModule,
    ScrollingModule,
    MatSlideToggleModule,
    CommonModule,
    MatFormFieldModule,
    MatNativeDateModule,
    MatInputModule
  ]
})
export class MaterialModule { }
