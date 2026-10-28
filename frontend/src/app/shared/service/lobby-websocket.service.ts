import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { wsUrl } from 'src/environments/api.config';

export interface LobbyRealtimeEvent {
  type: 'PERFIL_UPDATED' | 'MESA_CREADA' | 'MESA_CERRADA' | 'MESA_INICIADA' | string;
  jugadorId?: string;
  nombre?: string;
  avatar?: string;
  partidaId?: string;
  hostJugadorId?: string;
  jugadorIds?: string[];
  partida?: any;
}

/**
 * Global lobby channel: /ws/lobby (perfil + mesas). Separate from partida rooms.
 */
@Injectable()
export class LobbyWebsocketService {
  private socket?: WebSocket;
  private reconnectTimer?: ReturnType<typeof setTimeout>;
  private intentionalClose = false;
  private readonly eventsSubject = new Subject<LobbyRealtimeEvent>();

  readonly events$: Observable<LobbyRealtimeEvent> = this.eventsSubject.asObservable();

  connect(): void {
    if (this.socket && this.socket.readyState <= WebSocket.OPEN) {
      return;
    }
    this.intentionalClose = false;
    this.openSocket();
  }

  disconnect(): void {
    this.intentionalClose = true;
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = undefined;
    }
    if (this.socket) {
      this.socket.close();
      this.socket = undefined;
    }
  }

  private openSocket(): void {
    const url = wsUrl('/lobby');
    this.socket = new WebSocket(url);

    this.socket.onopen = () => console.log('Lobby WS connected', url);
    this.socket.onmessage = (event: MessageEvent) => {
      try {
        this.eventsSubject.next(JSON.parse(event.data) as LobbyRealtimeEvent);
      } catch (err) {
        console.error('Invalid lobby WS payload', err);
      }
    };
    this.socket.onerror = (err) => console.error('Lobby WS error', err);
    this.socket.onclose = () => {
      this.socket = undefined;
      if (!this.intentionalClose) {
        this.reconnectTimer = setTimeout(() => this.openSocket(), 2500);
      }
    };
  }
}
