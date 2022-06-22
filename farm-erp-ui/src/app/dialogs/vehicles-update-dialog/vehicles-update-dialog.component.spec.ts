import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiclesUpdateDialogComponent } from './vehicles-update-dialog.component';

describe('VehiclesUpdateDialogComponent', () => {
  let component: VehiclesUpdateDialogComponent;
  let fixture: ComponentFixture<VehiclesUpdateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VehiclesUpdateDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VehiclesUpdateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
