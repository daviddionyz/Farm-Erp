import {Component, OnInit, ViewChild} from '@angular/core';
import {HarvestService} from "../../service/harvest.service";
import {DialogService} from "../../service/dialog.service";
import {MatSelectionList} from "@angular/material/list";
import {Diary} from "../../models/harvest/Diary";
import {dashCaseToCamelCase} from "@angular/compiler/src/util";
import {RoleService} from "../../service/role.service";

@Component({
  selector: 'app-harvest',
  templateUrl: './harvest.component.html',
  styleUrls: ['./harvest.component.scss']
})
export class HarvestComponent implements OnInit {

  originalInputData: Diary[] = [];
  harvestDiaries: Diary[] = [];
  selectedDiary!: Diary | null;
  isDiarySelected = false;
  searchCriteria: string = "";

  @ViewChild('selectName') selectList!: MatSelectionList;

  constructor(
    private harvestService : HarvestService,
    private dialogService  : DialogService,
    private roleService    : RoleService,
  ) {

  }

  ngOnInit(): void {
    this.harvestService.getAllHarvestDiaryYear().subscribe(
      data => {
        console.log(data)
        if (data.code === 200) {
          this.harvestDiaries = data.data;
          this.originalInputData = data.data;
        } else {
          this.dialogService.openUnexpectedErrorDialog();
        }
      }
    )
  }

  createHarvestYear() {
    if (this.roleService.openPopUpIfNotBoss()){
      return
    }

    this.harvestService.openCreateDialog().subscribe(
      newDiary => {
        if (newDiary) {
          this.harvestService.createHarvestDiary(newDiary).subscribe(
            data => {
              console.log(data)
              if (data.code === 200) {
                this.dialogService.openSuccessDialog();
              } else {
                this.dialogService.openUnexpectedErrorDialog();
              }
              this.ngOnInit();
            }
          )
        }
      }
    )
  }

  deleteHarvestYear() {
    if (!this.selectedDiary) {
      return
    }
    if (this.roleService.openPopUpIfNotBoss()){
      return
    }

    this.dialogService.openConfirmDialog("Biztosan szeretné törölni?", "warn").afterClosed().subscribe(
      data => {
        if (data) {


          this.harvestService.deleteHarvestDiary(this.selectedDiary?.id ?? -1).subscribe(
            data => {
              console.log(data)
              if (data.code === 200) {
                this.dialogService.openSuccessDialog();
              } else {
                this.dialogService.openUnexpectedErrorDialog();
              }
              this.ngOnInit();
            }
          )

        }
      }
    )

  }


  onOpen() {
    if (!this.selectedDiary) {
      return
    }

    this.isDiarySelected = true;
  }

  onSelection() {
    this.selectedDiary = this.selectList.selectedOptions.selected[0].value
  }

  search() {
    if (this.searchCriteria === '') {
      this.harvestDiaries = this.originalInputData;
    } else {
      this.harvestDiaries = [];
      this.originalInputData.forEach(diary => {
        if (diary.name.toLowerCase().includes(this.searchCriteria.toLowerCase())) {
          this.harvestDiaries.push(diary);
        }
      })
    }
  }

  onBack() {
    this.isDiarySelected = false;
    this.selectedDiary = null;
  }
}
