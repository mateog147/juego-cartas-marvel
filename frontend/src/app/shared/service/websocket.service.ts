import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { wsUrl } from 'src/environments/api.config';

export interface PartidaRealtimeEvent {
  type: 'BET_PLACED' | 'ROUND_RESOLVED' | 'STATE' | 'PARTIDA_CERRADA' | 'PARTIDA_INICIADA' | string;
  partidaId: string;
  partida: any;
}

/**
 * One WebSocket room per partida: /ws/partida/{partidaId}
 */
@Injectable()
export class WebsocketService {

  private socket?: WebSocket;
  private partidaId?: string;
  private readonly eventsSubject = new Subject<PartidaRealtimeEvent>();

  /** Stream of typed room events for the active partida. */
  readonly events$: Observable<PartidaRealtimeEvent> = this.eventsSubject.asObservable();

  /** @deprecated kept for older callers; prefer events$ */
  get messages(): Subject<any> {
    return this.eventsSubject as Subject<any>;
  }

  connectToPartida(partidaId: string): void {
    if (!partidaId) {
      return;
    }
    if (this.socket && this.partidaId === partidaId && this.socket.readyState <= WebSocket.OPEN) {
      return;
    }
    this.disconnect();
    this.partidaId = partidaId;
    const url = wsUrl(`/partida/${partidaId}`);
    this.socket = new WebSocket(url);

    this.socket.onopen = () => console.log('WS room connected', url);
    this.socket.onmessage = (event: MessageEvent) => {
      try {
        const parsed = JSON.parse(event.data) as PartidaRealtimeEvent;
        this.eventsSubject.next(parsed);
      } catch (err) {
        console.error('Invalid WS payload', err);
      }
    };
    this.socket.onerror = (err) => console.error('WS room error', err);
    this.socket.onclose = () => console.log('WS room closed', partidaId);
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = undefined;
    }
    this.partidaId = undefined;
  }
}
