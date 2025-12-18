import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DealEdit } from './deal-edit';

describe('DealEdit', () => {
  let component: DealEdit;
  let fixture: ComponentFixture<DealEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DealEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DealEdit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
