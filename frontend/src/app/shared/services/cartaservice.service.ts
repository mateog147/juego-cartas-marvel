import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Card } from 'src/app/components/card/card.component';
import { apiUrl } from 'src/environments/api.config';

@Injectable({
  providedIn: 'root'
})

//TODO: Corregir nombre
export class CartaserviceService {

  private readonly cartaUrl = apiUrl('/carta/');

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get(this.cartaUrl, this.httpOptions);
  }

  crearNuevaCarta(carta: Card): Observable<any> {
    return this.http.post(this.cartaUrl, carta, this.httpOptions);
  }

  eliminarCarta(cartaId: string): Observable<any> {
    return this.http.delete(`${this.cartaUrl}${cartaId}`, this.httpOptions);
  }

  editCarta(cartaId: string, carta: Card): Observable<any> {
    return this.http.put(`${this.cartaUrl}${cartaId}`, carta, this.httpOptions);
  }
}
