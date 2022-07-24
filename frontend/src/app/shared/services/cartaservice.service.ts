import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Card } from 'src/app/components/card/card.component';

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
  crearNuevaCarta( carta: Card ) : Observable<any> {
    return this.http.post(this.cartaURL, carta, this.httpOptions)
  }

  eliminarCarta( cartaId: string ) : Observable<any>{
    return this.http.delete(`${this.cartaURL}/${cartaId}`, this.httpOptions)
  }
  editCarta( cartaId: string , carta: Card ): Observable<any>{
       return this.http.put(`${this.cartaURL}/${cartaId}`, carta , this.httpOptions)
  }

}
