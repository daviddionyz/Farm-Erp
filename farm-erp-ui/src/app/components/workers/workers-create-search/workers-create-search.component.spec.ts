import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkersCreateSearchComponent } from './workers-create-search.component';

describe('WorkersCreateSearchComponent', () => {
  let component: WorkersCreateSearchComponent;
  let fixture: ComponentFixture<WorkersCreateSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkersCreateSearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkersCreateSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
