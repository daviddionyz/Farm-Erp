import {Component, OnInit} from '@angular/core';
import {FieldsService} from "../../service/fields.service";
import {Field} from "../../models/fields/field";
import {MatTableDataSource} from "@angular/material/table";
import {DialogService} from "../../service/dialog.service";
import {AuthService} from "../../service/auth.service";
import {User} from "../../models/User";
import {Roles} from "../../enums/Roles";
import {RoleService} from "../../service/role.service";
import {PageRequestFields} from "../../models/page-requests/page-request-fields";

@Component({
  selector: 'app-fields',
  templateUrl: './fields.component.html',
  styleUrls: ['./fields.component.scss']
})
export class FieldsComponent implements OnInit {

  dataSource      : MatTableDataSource<Field>;
  allFieldNumber  : number;
  pageRequest     : PageRequestFields;

  constructor(
    private fieldService  : FieldsService,
    private dialogService : DialogService,
    public roleService    : RoleService,
  ) {
    this.dataSource     = new MatTableDataSource();
    this.allFieldNumber = 0;
    this.pageRequest = {
      page     : 0,
      pageSize : 10,
      sortBy   : 'name',
      direction: 'ASC',

      name     : '',
      corpType : '',
      corpName : '',
    }
  }

  ngOnInit(): void {
    this.fieldService.getALlField(this.pageRequest).subscribe(
      data => {
        console.log(data);
        this.dataSource.data = data.data.objects[0];
        this.allFieldNumber = data.data.allNumber;
      },
      error => console.error(error)
    );
  }

  update(field: Field) {
    if (this.roleService.openPopUpIfNotBoss()){
      return
    }

    this.fieldService.openUpdateDialog(Object.assign({}, field)).subscribe(
      data => {
        if (data) {
          this.fieldService.updateField(data).subscribe(
            data => {
              console.log(data);
              if (data.code === 200) {
                this.dialogService.openDialog("Sikeres frissítés", "info");
              } else {
                this.dialogService.openDialog("Sikertelen frissítés", "info");
              }
              this.ngOnInit();
            });
        }
      });
  }

  delete(field: Field) {
    if (this.roleService.openPopUpIfNotBoss()){
      return
    }

    this.dialogService.openConfirmDialog("Biztosan szeretné törölni?", "warn").afterClosed().subscribe( res => {
      if (res){
        this.fieldService.deleteField(field.id ?? -1).subscribe(
          data => {
            console.log(data);
            if (data.code === 200) {
              this.dialogService.openDialog("Sikeres törlés", "info");
            } else {
              this.dialogService.openDialog("Sikertelen törlés", "info");
            }
            this.ngOnInit();
          });
      }
    });
  }

  create(field: Field) {
    this.fieldService.createNewField(field).subscribe(
      data => {
        console.log(data);
        if (data.code === 200) {
          this.dialogService.openDialog("Sikeresen létrehozva", "info");
        } else {
          this.dialogService.openDialog("Sikertelen művelet", "info");
        }
        this.ngOnInit();
      });
  }

  search(field: Field) {
    this.pageRequest.name     = field.name;
    this.pageRequest.corpType = field.corpType;
    this.pageRequest.corpName = field.corpName;

    this.ngOnInit();
  }

  clear() {
    this.pageRequest.name     = '';
    this.pageRequest.corpType = '';
    this.pageRequest.corpName = '';

    this.pageRequest.page     = 0;
    this.pageRequest.pageSize = 10;

    this.ngOnInit();
  }

}
