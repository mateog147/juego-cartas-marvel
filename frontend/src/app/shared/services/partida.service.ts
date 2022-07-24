import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';

@Injectable({
  providedIn: 'root'
})
export class PartidaService {

  private partidaURl = '/api/partida/';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor( private http: HttpClient) { }
  
  getPartidaporId(partidaId: string){
    return this.http.get(this.partidaURl + partidaId )
  }

  enviarApuesta(idPartida : string, apuesta: ApuestaModel): Observable<any>{
    return this.http.post(this.partidaURl + idPartida, apuesta, this.httpOptions)
  }

  ganadorRonda(partidaId : string): Observable<any>{
    return this.http.get(`${this.partidaURl}ganador/${partidaId}`)
  }

  crearPartida(jugadores : string[]): Observable<any>{
    return this.http.put(this.partidaURl, jugadores, this.httpOptions)

  }

  
  
}
