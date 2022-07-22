import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvatarjugadorComponent } from './avatarjugador.component';

describe('AvatarjugadorComponent', () => {
  let component: AvatarjugadorComponent;
  let fixture: ComponentFixture<AvatarjugadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AvatarjugadorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvatarjugadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
