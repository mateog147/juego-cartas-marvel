import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Card } from 'src/app/components/card/card.component';
import { ApuestaModel } from 'src/app/models/apuesta.model';
import { Jugador } from './jugador';

@Injectable({
  providedIn: 'root'
})
export class JugadorserviceService {

  private jugadorURl = '/api/jugador/';
  private partidaURl = '/api/partida/';

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

  enviarApuesta(idPartida : string, apuesta: ApuestaModel): Observable<any>{
    return this.http.post(this.partidaURl + idPartida, apuesta, this.httpOptions)
  }

  ganadorRonda(idPartida : string): Observable<any>{
    return this.http.get(`${this.partidaURl}/ganador/${idPartida}`)
  }


}
