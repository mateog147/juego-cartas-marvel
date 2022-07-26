import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Jugador } from '../../interface/jugador';

@Injectable({
  providedIn: 'root'
})
export class JugadorserviceService {

  private jugadorURl = 'https://sofkau-marvels.herokuapp.com/api/jugador/';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  constructor( private http: HttpClient) { }

  nuevoJugador(uid: string, name: string): Observable<any> {
    const data: Jugador = {
      nombre: name,
      uid: uid,
		  cartas : [],
      puntaje: 0
    }
    return this.http.post(this.jugadorURl, data, this.httpOptions)
  }

  getJugadoresDB() : Observable<any>{
   return this.http.get(this.jugadorURl, this.httpOptions)
  }


}
