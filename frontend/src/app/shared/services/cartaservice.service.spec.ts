import { TestBed } from '@angular/core/testing';

import { CartaserviceService } from './cartaservice.service';

describe('CartaserviceService', () => {
  let service: CartaserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CartaserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
