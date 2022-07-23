import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Jugador } from './jugador';

@Injectable({
  providedIn: 'root'
})
export class JugadorserviceService {

  private jugadorURl = '/api/carta/';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor( private http: HttpClient) { }
  
  nuevoJugador() :Observable<any> {
    const data: Jugador = {
      nombre: 'SPIDER MAN',
      xp: 345621222,  		   
		  imagen : "../../../assets/imgs/spider1.jpg"
    }
    return this.http.post(this.jugadorURl, data, this.httpOptions)
  }

}
