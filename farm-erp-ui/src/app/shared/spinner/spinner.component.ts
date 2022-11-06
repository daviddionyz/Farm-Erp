import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss'],
})
export class SpinnerComponent{

  constructor(
    private spinner : NgxSpinnerService,
    public dialogRef: MatDialogRef<SpinnerComponent>) {
    spinner.show().then(r => {});
  }


}
