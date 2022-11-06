import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../service/storage.service";
import {MatTableDataSource} from "@angular/material/table";
import {Storage} from "../../models/storage/storage";
import {DialogService} from "../../service/dialog.service";
import {RoleService} from "../../service/role.service";
import {PageRequestStorage} from "../../models/page-requests/page-request-storage";

@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss']
})
export class StorageComponent implements OnInit {

  dataSource: MatTableDataSource<Storage>;
  pageRequest: PageRequestStorage
  allFieldNumber: number;

  constructor(
    private storageService: StorageService,
    private dialogService: DialogService,
    public roleService: RoleService,
  ) {
    this.dataSource = new MatTableDataSource();
    this.allFieldNumber = 0;
    this.pageRequest = {
      page: 0,
      pageSize: 20,
      sortBy: "",
      direction: "",

      name: ''
    }
  }

  ngOnInit(): void {
    this.storageService.getALlStorage(this.pageRequest).subscribe(
      data => {
        console.log(data);
        this.dataSource.data = data.data.objects[0];
        this.allFieldNumber = data.data.allNumber;
      });
  }

  update(storage: Storage) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.storageService.openUpdateDialog(Object.assign({}, storage)).subscribe(
      updateDialogRes => {
        if (updateDialogRes) {
          this.storageService.updateStorage(updateDialogRes).subscribe(
            apiRes => {
              console.log(apiRes);
              if (apiRes.code === 200) {
                this.dialogService.openDialog("Sikeres frissítés", "info");
              } else {
                this.dialogService.openDialog("Sikertelen frissítés", "info");
              }
              this.ngOnInit();
            });
        }
      });
  }

  create(storage: Storage) {
    this.storageService.createNewStorage(storage).subscribe(
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

  delete(storage: Storage) {
    if (this.roleService.openPopUpIfNotBoss()) {
      return
    }

    this.dialogService.openConfirmDialog("Biztosan szeretné törölni?", "warn").afterClosed().subscribe(res => {
      if (res)
        this.storageService.deleteStorage(storage.id ?? -1).subscribe(
          data => {
            console.log(data);
            if (data.code === 200) {
              this.dialogService.openDialog("Sikeres törlés", "info");
            } else {
              this.dialogService.openDialog("Sikertelen törlés", "info");
            }
            this.ngOnInit();
          });
    });
  }


  search(storage: Storage) {
    this.pageRequest.name = storage.name

    this.ngOnInit();
  }

  clear() {
    this.pageRequest.name = ''

    this.pageRequest.page = 0;
    this.pageRequest.pageSize = 10;

    this.ngOnInit();
  }

  cropsInfo(storage: Storage) {
    this.storageService.getCropsInfo(storage?.id ?? -1).subscribe(
      data => {
        console.log(data)
        if (data.data && (data.data as any[]).length != 0) {
          this.storageService.openCropsDialog(data.data);
        } else {
          this.dialogService.openDialog("A raktárban nem találhato semmi.", "info");
        }
      });
  }


}
