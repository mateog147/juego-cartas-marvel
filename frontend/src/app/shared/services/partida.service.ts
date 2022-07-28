import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import {tap} from 'rxjs/operators';
import { JugadorId } from 'src/app/components/dashboard/dashboard.component';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';
//import { JugadorId } from 'src/app/interface/jugadorId';

@Injectable({
  providedIn: 'root'
})
export class PartidaService {

  private partidaURl = '/api/partida/';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor( private http: HttpClient) { }
  
  refresh$: Subject<void> = new Subject();

  getRefresh$(){
    return this.refresh$;
  }
  getPartidaporId(partidaId: string){
    return this.http.get(this.partidaURl + partidaId )
  }

  enviarApuesta(idPartida : string, apuesta: ApuestaModel): Observable<any>{
    return this.http.post(this.partidaURl + idPartida, apuesta, this.httpOptions)
    .pipe(tap(() => this.getRefresh$().next()))
  }

  ganadorRonda(partidaId : string): Observable<any>{
    return this.http.get(`${this.partidaURl}ganador/${partidaId}`)
  }

  crearPartida(jugadores : JugadorId[]): Observable<any>{
    return this.http.post(this.partidaURl, jugadores, this.httpOptions)

  }



}
