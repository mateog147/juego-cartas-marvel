import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Jugador } from '../../interface/jugador';
import { apiUrl } from 'src/environments/api.config';

@Injectable({
  providedIn: 'root'
})
export class JugadorserviceService {

  private readonly jugadorUrl = apiUrl('/jugador/');

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  nuevoJugador(uid: string, name: string): Observable<any> {
    const data: Jugador = {
      nombre: name,
      uid: uid,
      cartas: [],
      puntaje: 0
    };
    return this.http.post(this.jugadorUrl, data, this.httpOptions);
  }

  getJugadoresDB(): Observable<any> {
    return this.http.get(this.jugadorUrl, this.httpOptions);
  }
}
