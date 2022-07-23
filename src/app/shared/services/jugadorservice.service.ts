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
      nombre: 'WOLVERINE',
      xp: 12344,

      
		   
		   imagen : "../../../assets/imgs/lobezno.jpg"
    }
    return this.http.put(this.jugadorURl+'62db490108cf801d22e295f7', data, this.httpOptions)
  }

}
