import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablacrudComponent } from './tablacrud.component';

describe('TablacrudComponent', () => {
  let component: TablacrudComponent;
  let fixture: ComponentFixture<TablacrudComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TablacrudComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TablacrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
