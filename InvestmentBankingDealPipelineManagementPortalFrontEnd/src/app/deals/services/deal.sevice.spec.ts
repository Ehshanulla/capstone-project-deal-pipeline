import { TestBed } from '@angular/core/testing';

import { DealSevice } from './deal.sevice';

describe('DealSevice', () => {
  let service: DealSevice;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DealSevice);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
