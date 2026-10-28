import { Component, DoCheck, OnDestroy, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

import { Card } from '../card/card.component';
import { AuthService } from 'src/app/shared/services/auth.service';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';
import { PartidaService } from 'src/app/shared/services/partida.service';

import { Jugador } from 'src/app/interface/jugador';
import { ActivatedRoute, Router } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import { PartidaRealtimeEvent, WebsocketService } from 'src/app/shared/service/websocket.service';
import { comicAlert, comicError, comicInfo, comicWin } from 'src/app/shared/utils/comic-alert';

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
  jugadorDbId = '';
  time = 0;
  iniciando = false;
  private mostrandoGanador = false;
  private partidaCerradaAvisada = false;
  private accesoDenegadoAvisado = false;
  private timerSub?: Subscription;
  private wsSub?: Subscription;
  private lastKnownRound = 0;

  constructor(
    public authService: AuthService,
    private partidaService: PartidaService,
    private rutaActiva: ActivatedRoute,
    private router: Router,
    private websocket: WebsocketService,
  ) {}

  get esperandoInicio(): boolean {
    return this.partida?.estado === 'ESPERANDO';
  }

  get soyHost(): boolean {
    return !!this.jugadorDbId && this.partida?.hostJugadorId === this.jugadorDbId;
  }

  get enJuego(): boolean {
    const e = this.partida?.estado;
    return !e || e === 'EN_JUEGO' || e === 'ABIERTA';
  }

  ngDoCheck(): void {
    const jugadoresCount = this.partida?.jugadores?.length ?? 0;
    this.tablero.status = jugadoresCount > 0 && this.apuestas.length === jugadoresCount;
  }

  ngOnInit(): void {
    this.partidaId = this.rutaActiva.snapshot.paramMap.get('idPartida')!;
    this.jugadoruid = JSON.parse(localStorage.getItem('user')!).uid;
    this.jugadorDbId = this.partidaService.currentJugadorId();

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

    if (event.type === 'PARTIDA_CERRADA' || event.partida?.estado === 'CERRADA') {
      this.avisarPartidaCerrada(event.partida);
      return;
    }

    if (event.type === 'PARTIDA_INICIADA') {
      this.syncTimerFromServer();
      return;
    }

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
    if (snapshot?.estado === 'CERRADA') {
      this.avisarPartidaCerrada(snapshot);
    }
  }

  private avisarPartidaCerrada(partida: any) {
    if (this.partidaCerradaAvisada) {
      return;
    }
    this.partidaCerradaAvisada = true;
    this.timerSub?.unsubscribe();
    const ganador = partida?.jugadores?.length === 1
      ? (partida.jugadores[0]?.nombre || 'un jugador')
      : null;
    if (ganador) {
      comicWin('¡Campeón!', ganador, { confirmButtonText: 'Volver al lobby' })
        .then(() => this.router.navigate(['/dashboard']));
    } else {
      comicInfo('Mesa cerrada', 'La partida terminó.')
        .then(() => this.router.navigate(['/dashboard']));
    }
  }

  private avisarAccesoDenegado(msg?: string) {
    if (this.accesoDenegadoAvisado) {
      return;
    }
    this.accesoDenegadoAvisado = true;
    comicError('No podés entrar', msg || 'Esta mesa es solo para los jugadores invitados.')
      .then(() => this.router.navigate(['/dashboard']));
  }

  drop(event: CdkDragDrop<Card[]>) {
    if (this.esperandoInicio) {
      comicInfo('Aún no empieza', 'Esperá a que el host pulse Empezar.');
      return;
    }
    if (this.partida?.estado === 'CERRADA') {
      comicInfo('Mesa cerrada', 'Esta partida ya terminó.');
      return;
    }

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
      return;
    }

    if (this.filtrarJugadorEnapuesta().length > 0) {
      comicInfo('Ya apostaste', 'Esperá a los demás jugadores.');
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
    if (this.esperandoInicio) {
      this.time = 0;
      return;
    }
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
    this.partidaService.getPartidaporId(this.partidaId).subscribe({
      next: (item: any) => {
        this.partida = item;
        this.jugadores = item.jugadores || [];
        this.imprimir();
        after?.();
      },
      error: (err) => {
        if (err?.status === 403) {
          this.avisarAccesoDenegado(err?.error?.error);
          return;
        }
        comicError('No se pudo cargar la partida')
          .then(() => this.router.navigate(['/dashboard']));
      }
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
      if (jugador.uid === this.jugadoruid || jugador.id === this.jugadorDbId) {
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
        comicError(err?.error?.error || 'No se pudo enviar la apuesta');
        this.getPartidaPorId(() => this.syncTimerFromServer());
      }
    });
  }

  iniciarPartidaDesdeTablero() {
    if (!this.soyHost || !this.esperandoInicio || this.iniciando) {
      return;
    }
    this.iniciando = true;
    this.partidaService.iniciarPartida(this.partidaId, this.jugadorDbId).subscribe({
      next: (partida) => {
        this.iniciando = false;
        this.applyPartidaSnapshot(partida);
      },
      error: (err) => {
        this.iniciando = false;
        comicError(err?.error?.error || 'No se pudo empezar la partida');
      }
    });
  }

  private mostrarGanadorRonda(nombre: string) {
    if (this.mostrandoGanador) {
      return;
    }
    this.mostrandoGanador = true;
    this.lastKnownRound = this.partida?.ronda?.numero ?? this.lastKnownRound;
    comicWin(`Gana la ronda`, nombre, {
      timer: 4000,
      timerProgressBar: true,
      showConfirmButton: true,
      confirmButtonText: 'Boom',
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
    if (this.esperandoInicio) {
      return;
    }
    if (this.partida?.estado === 'CERRADA' || this.partida?.jugadores?.length === 1) {
      this.avisarPartidaCerrada(this.partida);
    } else if (this.jugadorInfo && (this.jugadorInfo.cartas?.length ?? 0) === 0) {
      comicAlert({
        title: '¡KO!',
        html: '<div class="comic-swal-burst">OUT</div><p class="comic-swal-sub">Te quedaste sin cartas.</p>',
        confirmButtonText: 'Seguir mirando',
      });
    }
  }

  cerrarMesaDesdeTablero() {
    if (!this.partidaId || this.partida?.estado === 'CERRADA') {
      return;
    }
    comicAlert({
      title: '¿Cerrar la mesa?',
      text: 'Se termina la partida para todos.',
      showCancelButton: true,
      confirmButtonText: 'Cerrar',
      cancelButtonText: 'Seguir',
    }).then(result => {
      if (!result.isConfirmed) {
        return;
      }
      this.partidaService.cerrarPartida(this.partidaId, this.jugadorDbId).subscribe({
        next: (partida) => this.avisarPartidaCerrada(partida),
        error: (err) => comicError(err?.error?.error || 'No se pudo cerrar la mesa')
      });
    });
  }
}
