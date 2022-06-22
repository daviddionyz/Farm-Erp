import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Field} from "../../../models/fields/field";

@Component({
  selector: 'app-field-create-search',
  templateUrl: './field-create-search.component.html',
  styleUrls: ['./field-create-search.component.scss']
})
export class FieldCreateSearchComponent implements OnInit {

  newField : Field = {
    id      : 0,
    name    : '',
    size    : 0,
    corpType: '',
    corpName: '',
  }

  okButtonText: string = "";

  @Input() mode!: string;

  @Output() submit: EventEmitter<Field> = new EventEmitter<Field>();
  @Output() clear: EventEmitter<any> = new EventEmitter<any>();

  constructor() {

  }

  ngOnInit(): void {
    this.textInit();
  }

  textInit(){
    switch (this.mode) {
      case 'search':
        this.okButtonText = 'Keresés'
        break;
      case 'create':
        this.okButtonText = 'Létrehozás'
        break;
    }
  }

  onClear() {
    this.newField.name     = '';
    this.newField.size     = 0;
    this.newField.corpType = '';
    this.newField.corpName = '';

    this.clear.emit();
  }

  onSubmit() {
    this.submit.emit(this.newField);
    this.onClear();
  }

  checkIdfDataIsGiven() {
    switch (this.mode) {
      case 'create':
        return !(this.newField.name !== '' && this.newField.corpType !== '' && this.newField.corpName !== '' && Number.isInteger(this.newField.size))
      default:
        return false
    }
  }
}
