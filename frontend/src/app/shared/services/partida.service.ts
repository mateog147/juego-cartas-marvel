import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { JugadorId } from 'src/app/components/dashboard/dashboard.component';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';
import { apiUrl } from 'src/environments/api.config';

@Injectable({
  providedIn: 'root'
})
export class PartidaService {

  private readonly partidaUrl = apiUrl('/partida/');

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  refresh$: Subject<void> = new Subject();

  getRefresh$() {
    return this.refresh$;
  }

  /** Mongo jugador id from localStorage (roster membership). */
  currentJugadorId(): string {
    try {
      const raw = localStorage.getItem('jugador');
      if (!raw) {
        return '';
      }
      return JSON.parse(raw)?.id || '';
    } catch {
      return '';
    }
  }

  private withJugador(url: string, jugadorId?: string): string {
    const id = jugadorId ?? this.currentJugadorId();
    if (!id) {
      return url;
    }
    const sep = url.includes('?') ? '&' : '?';
    return `${url}${sep}jugadorId=${encodeURIComponent(id)}`;
  }

  getPartidaporId(partidaId: string, jugadorId?: string) {
    return this.http.get(this.withJugador(this.partidaUrl + partidaId, jugadorId));
  }

  listarAbiertas(jugadorId: string): Observable<any[]> {
    const q = jugadorId ? `?jugadorId=${encodeURIComponent(jugadorId)}` : '';
    return this.http.get<any[]>(`${this.partidaUrl}abiertas${q}`);
  }

  iniciarPartida(partidaId: string, jugadorId?: string): Observable<any> {
    return this.http.post(
      this.withJugador(`${this.partidaUrl}iniciar/${partidaId}`, jugadorId),
      {},
      this.httpOptions
    ).pipe(tap(() => this.getRefresh$().next()));
  }

  cerrarPartida(partidaId: string, jugadorId?: string): Observable<any> {
    return this.http.post(
      this.withJugador(`${this.partidaUrl}cerrar/${partidaId}`, jugadorId),
      {},
      this.httpOptions
    ).pipe(tap(() => this.getRefresh$().next()));
  }

  enviarApuesta(idPartida: string, apuesta: ApuestaModel, jugadorId?: string): Observable<any> {
    return this.http.post(
      this.withJugador(this.partidaUrl + idPartida, jugadorId || apuesta.jugadorId),
      apuesta,
      this.httpOptions
    ).pipe(tap(() => this.getRefresh$().next()));
  }

  ganadorRonda(partidaId: string): Observable<any> {
    return this.http.get(`${this.partidaUrl}ganador/${partidaId}`);
  }

  crearPartida(jugadores: JugadorId[]): Observable<any> {
    return this.http.post(this.partidaUrl, jugadores, this.httpOptions);
  }
}
