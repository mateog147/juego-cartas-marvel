import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartaserviceService {

  private cartaURL = '/api/carta/';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }
  
  getAll() : Observable<any>{
    return this.http.get(this.cartaURL, this.httpOptions)
    
  }

}
