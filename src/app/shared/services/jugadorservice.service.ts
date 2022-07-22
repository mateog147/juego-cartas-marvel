import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Jugador } from './jugador';

@Injectable({
  providedIn: 'root'
})
export class JugadorserviceService {

  private jugadorURl = '/api/jugador/crear';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor( private http: HttpClient) { }
  
  nuevoJugador(uid: string, nombre: string) :Observable<any> {
    const data: Jugador = {
      nombre: nombre,
      uid: uid,
      cartas: [],
      puntaje: 0,
    }
    return this.http.post(this.jugadorURl, data, this.httpOptions)
  }

}
