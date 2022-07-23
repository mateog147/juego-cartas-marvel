import { TestBed } from '@angular/core/testing';

import { JugadorserviceService } from './jugadorservice.service';

describe('JugadorserviceService', () => {
  let service: JugadorserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JugadorserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
