import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Piloto } from './piloto';

describe('Piloto', () => {
  let component: Piloto;
  let fixture: ComponentFixture<Piloto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Piloto],
    }).compileComponents();

    fixture = TestBed.createComponent(Piloto);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
