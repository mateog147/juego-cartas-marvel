import { Component, DoCheck, OnDestroy, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

import { Card } from '../card/card.component';
import { AuthService } from 'src/app/shared/services/auth.service';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';
import { PartidaService } from 'src/app/shared/services/partida.service';

import { Jugador } from 'src/app/interface/jugador';
import { ActivatedRoute } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { PartidaRealtimeEvent, WebsocketService } from 'src/app/shared/service/websocket.service';

@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css'],
})
export class TableroComponent implements OnInit, DoCheck, OnDestroy {

  tablero: { status: boolean } = { status: false };
  partidaId!: string;
  ronda: number = 0;
  apuestas: { jugadorId: string, carta: Card }[] = [];
  mazo: Card[] = [];
  apuestadrop: Card[] = [];
  jugadores: any[] = [];
  partida: any;
  jugadoruid: any;
  jugadorInfo: any;
  time = 0;
  private mostrandoGanador = false;
  private timerSub?: Subscription;
  private wsSub?: Subscription;
  private lastKnownRound = 0;

  constructor(
    public authService: AuthService,
    private partidaService: PartidaService,
    private rutaActiva: ActivatedRoute,
    private websocket: WebsocketService,
  ) {}

  ngDoCheck(): void {
    const jugadoresCount = this.partida?.jugadores?.length ?? 0;
    this.tablero.status = jugadoresCount > 0 && this.apuestas.length === jugadoresCount;
  }

  ngOnInit(): void {
    this.partidaId = this.rutaActiva.snapshot.paramMap.get('idPartida')!;
    this.jugadoruid = JSON.parse(localStorage.getItem('user')!).uid;

    this.websocket.connectToPartida(this.partidaId);
    this.wsSub = this.websocket.events$.subscribe(event => this.onRealtimeEvent(event));

    this.getPartidaPorId(() => {
      this.syncTimerFromServer();
      this.chequearGanadorPartida();
    });
  }

  ngOnDestroy(): void {
    this.timerSub?.unsubscribe();
    this.wsSub?.unsubscribe();
    this.websocket.disconnect();
  }

  private onRealtimeEvent(event: PartidaRealtimeEvent) {
    if (!event?.partida || event.partidaId !== this.partidaId) {
      return;
    }

    this.applyPartidaSnapshot(event.partida);

    if (event.type === 'ROUND_RESOLVED') {
      const nombre = event.partida?.ronda?.ultimoGanador;
      if (nombre) {
        this.mostrarGanadorRonda(nombre);
      }
    }
  }

  private applyPartidaSnapshot(snapshot: any) {
    this.partida = snapshot;
    this.jugadores = snapshot.jugadores || [];
    this.imprimir();
    this.syncTimerFromServer();
    this.chequearGanadorPartida();
  }

  drop(event: CdkDragDrop<Card[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
      return;
    }

    if (this.filtrarJugadorEnapuesta().length > 0) {
      Swal.fire('Ya hiciste tu apuesta, espera a los demas jugadores');
      return;
    }

    transferArrayItem(
      event.previousContainer.data,
      event.container.data,
      event.previousIndex,
      event.currentIndex,
    );

    this.newArray();
    const cartaApostada: Card = event.container.data[0];
    this.enviarApuesta(this.partidaId, cartaApostada);
    this.renderTableroApuestas();
  }

  /** Countdown driven by server deadline (finRondaEn). */
  syncTimerFromServer() {
    this.timerSub?.unsubscribe();
    const endsAt = this.partida?.ronda?.finRondaEn;
    if (!endsAt) {
      this.time = this.partida?.ronda?.isTimerOn ?? 30;
      return;
    }

    this.time = Math.max(0, Math.ceil((endsAt - Date.now()) / 1000));
    this.timerSub = interval(250).subscribe(() => {
      this.time = Math.max(0, Math.ceil((endsAt - Date.now()) / 1000));
      if (this.time === 0) {
        this.timerSub?.unsubscribe();
        // Server closes the round; room event ROUND_RESOLVED / BET_PLACED will refresh state
        setTimeout(() => {
          if (!this.mostrandoGanador) {
            this.getPartidaPorId(() => this.syncTimerFromServer());
          }
        }, 900);
      }
    });
  }

  filtrarJugadorEnapuesta() {
    if (!this.jugadorInfo) {
      return [];
    }
    return this.apuestas.filter(item => item.jugadorId === this.jugadorInfo.id);
  }

  newArray() {
    localStorage.setItem('mazo', JSON.stringify(this.mazo));
    localStorage.setItem('apuestadrop', JSON.stringify(this.apuestadrop));
  }

  imprimir() {
    this.renderTableroApuestas();
    this.getJugadorInfo();
    this.getMazo();
  }

  getPartidaPorId(after?: () => void) {
    this.partidaService.getPartidaporId(this.partidaId).subscribe((item: any) => {
      this.partida = item;
      this.jugadores = item.jugadores || [];
      this.imprimir();
      after?.();
    });
  }

  renderTableroApuestas() {
    this.apuestas = this.partida?.ronda?.apuestas?.length > 0
      ? this.partida.ronda.apuestas
      : [];
    this.ronda = this.partida?.ronda?.numero ?? 0;
  }

  getJugadorInfo() {
    this.partida?.jugadores?.forEach((jugador: Jugador) => {
      if (jugador.uid === this.jugadoruid) {
        this.jugadorInfo = jugador;
      }
    });
  }

  getMazo() {
    this.mazo = this.jugadorInfo?.cartas || [];
  }

  enviarApuesta(partidaId: string, carta: Card) {
    if (!this.jugadorInfo) {
      return;
    }

    const apuesta: ApuestaModel = {
      jugadorId: this.jugadorInfo.id,
      carta: {
        id: carta.id,
        nombre: carta.nombre,
        xp: carta.xp,
        imagen: carta.imagen
      }
    };

    this.partidaService.enviarApuesta(partidaId, apuesta).subscribe({
      next: (partidaActualizada: any) => {
        this.apuestadrop = [];
        this.applyPartidaSnapshot(partidaActualizada);

        if (partidaActualizada?.ronda?.ultimoGanador
            && (partidaActualizada.ronda.apuestas?.length ?? 0) === 0) {
          this.mostrarGanadorRonda(partidaActualizada.ronda.ultimoGanador);
        }
      },
      error: (err) => {
        console.error('Error enviando apuesta', err);
        Swal.fire('No se pudo enviar la apuesta');
        this.getPartidaPorId(() => this.syncTimerFromServer());
      }
    });
  }

  private mostrarGanadorRonda(nombre: string) {
    if (this.mostrandoGanador) {
      return;
    }
    this.mostrandoGanador = true;
    this.lastKnownRound = this.partida?.ronda?.numero ?? this.lastKnownRound;
    Swal.fire({
      timer: 4000,
      timerProgressBar: true,
      html: `<h2>El ganador de la ronda: ${nombre}</h2><hr/>
             <span style='font-size:100px;'>&#128526;</span>`
    }).then(() => {
      this.mostrandoGanador = false;
      this.getPartidaPorId(() => {
        this.apuestadrop = [];
        this.syncTimerFromServer();
        this.chequearGanadorPartida();
      });
    });
  }

  private chequearGanadorPartida() {
    if (this.partida?.jugadores?.length === 1) {
      const nombre = this.partida.jugadores[0]?.nombre || this.partida.ronda?.ultimoGanador;
      Swal.fire(`<h2>El ganador del juego fue: ${nombre}</h2><hr/>
                 <span style='font-size:100px;'>&#129321;</span>`);
    } else if (this.jugadorInfo && (this.jugadorInfo.cartas?.length ?? 0) === 0) {
      Swal.fire('Te quedaste sin cartas. Has perdido esta partida.');
    }
  }
}
