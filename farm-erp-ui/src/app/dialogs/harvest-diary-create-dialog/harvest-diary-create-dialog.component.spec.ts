import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HarvestDiaryCreateDialogComponent } from './harvest-diary-create-dialog.component';

describe('HarvestDiaryCreateDialogComponent', () => {
  let component: HarvestDiaryCreateDialogComponent;
  let fixture: ComponentFixture<HarvestDiaryCreateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HarvestDiaryCreateDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HarvestDiaryCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
