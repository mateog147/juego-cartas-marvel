import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Jugador } from 'src/app/interface/jugador';
import { AuthService } from 'src/app/shared/services/auth.service';
import { JugadorserviceService } from 'src/app/shared/services/jugadorservice.service';
import { PartidaService } from 'src/app/shared/services/partida.service';
import { LobbyRealtimeEvent, LobbyWebsocketService } from 'src/app/shared/service/lobby-websocket.service';
import { Clipboard } from '@angular/cdk/clipboard';
import { resolveAvatar } from 'src/app/shared/utils/avatars';
import { comicAlert } from 'src/app/shared/utils/comic-alert';

export interface JugadorId {
  id: string;
}

export interface MesaAbierta {
  id: string;
  estado?: string;
  hostJugadorId?: string;
  creadaEn?: number;
  jugadores?: Jugador[];
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  linkPartida = '';
  baseUrl = `${window.location.origin}/tablero/`;
  idPartida = '';
  partidaEstado = '';
  jugadores: string[] = [];
  usuarios: Jugador[] = [];
  rivales: Jugador[] = [];
  jugadorLog!: Jugador;
  cargandoJugadores = true;
  creandoPartida = false;
  iniciandoPartida = false;
  linkCopiado = false;
  errorLobby = '';
  mesasAbiertas: MesaAbierta[] = [];
  cargandoMesas = false;
  cerrandoMesaId = '';
  iniciandoMesaId = '';

  private lobbySub?: Subscription;
  private notifiedMesaIds = new Set<string>();

  constructor(
    public authService: AuthService,
    private partida: PartidaService,
    private router: Router,
    private jugador: JugadorserviceService,
    private clipboard: Clipboard,
    private lobbyWs: LobbyWebsocketService,
  ) {}

  ngOnInit(): void {
    this.todosJugadores();
    this.lobbyWs.connect();
    this.lobbySub = this.lobbyWs.events$.subscribe((event) => this.onLobbyEvent(event));
  }

  ngOnDestroy(): void {
    this.lobbySub?.unsubscribe();
    this.lobbyWs.disconnect();
  }

  @HostListener('document:visibilitychange')
  onVisibilityChange(): void {
    if (document.visibilityState === 'visible') {
      this.refrescarLobby();
    }
  }

  get totalEnMesa(): number {
    return this.jugadores.length;
  }

  get puedeCrear(): boolean {
    return this.totalEnMesa >= 2 && this.totalEnMesa <= 6 && !this.creandoPartida && !this.linkPartida;
  }

  get partidaLista(): boolean {
    return !!this.linkPartida && !!this.idPartida;
  }

  get mesaEnEspera(): boolean {
    return this.partidaLista && this.partidaEstado === 'ESPERANDO';
  }

  get avatarUrl(): string {
    return resolveAvatar(this.jugadorLog?.avatar, this.authService.userData?.photoURL);
  }

  avatarDe(j?: Jugador | null): string {
    return resolveAvatar(j?.avatar);
  }

  etiquetaJugador(j: Jugador): string {
    const nombre = j.nombre || 'Jugador';
    const shortUid = (j.uid || j.id || '').slice(-4).toUpperCase();
    return shortUid ? `${nombre} · #${shortUid}` : nombre;
  }

  etiquetaMesa(mesa: MesaAbierta): string {
    const shortId = (mesa.id || '').slice(-4).toUpperCase();
    const count = mesa.jugadores?.length ?? 0;
    return `Mesa #${shortId || '????'} · ${count} jugador${count === 1 ? '' : 'es'}`;
  }

  etiquetaEstado(estado?: string): string {
    if (estado === 'ESPERANDO') {
      return 'Esperando';
    }
    if (estado === 'EN_JUEGO' || estado === 'ABIERTA' || !estado) {
      return 'En juego';
    }
    if (estado === 'CERRADA') {
      return 'Cerrada';
    }
    return estado || '';
  }

  esHost(mesa: MesaAbierta): boolean {
    return !!this.jugadorLog?.id && mesa.hostJugadorId === this.jugadorLog.id;
  }

  puedeIniciarMesa(mesa: MesaAbierta): boolean {
    return this.esHost(mesa) && mesa.estado === 'ESPERANDO';
  }

  estaSeleccionado(j: Jugador): boolean {
    return !!j.id && this.jugadores.includes(j.id);
  }

  todosJugadores() {
    this.cargandoJugadores = true;
    this.errorLobby = '';
    const user = JSON.parse(localStorage.getItem('jugador')!);
    this.jugadores = [user.id];
    this.rivales = [];
    this.usuarios = [];
    this.jugadorLog = user;

    this.jugador.getJugadoresDB().subscribe({
      next: (data) => {
        this.usuarios = (data || []).filter((j: Jugador) => j.id !== user.id);
        this.cargandoJugadores = false;
      },
      error: () => {
        this.cargandoJugadores = false;
        this.errorLobby = 'No se pudieron cargar los jugadores. ¿Está el backend en marcha?';
      }
    });

    this.cargarMesasAbiertas();
  }

  refrescarLobby() {
    if (!this.jugadorLog?.id) {
      return;
    }
    const myId = this.jugadorLog.id;
    this.jugador.getJugadoresDB().subscribe({
      next: (data) => {
        this.usuarios = (data || []).filter((j: Jugador) => j.id !== myId);
        const me = (data || []).find((j: Jugador) => j.id === myId);
        if (me) {
          this.jugadorLog = { ...this.jugadorLog, ...me };
          localStorage.setItem('jugador', JSON.stringify(this.jugadorLog));
        }
        this.rivales = this.rivales.map((rival) => {
          const fresh = (data || []).find((j: Jugador) => j.id === rival.id);
          return fresh ? { ...rival, ...fresh } : rival;
        });
      }
    });
    this.cargarMesasAbiertas();
  }

  cargarMesasAbiertas() {
    if (!this.jugadorLog?.id) {
      return;
    }
    this.cargandoMesas = true;
    this.partida.listarAbiertas(this.jugadorLog.id).subscribe({
      next: (data) => {
        this.mesasAbiertas = Array.isArray(data) ? data : [];
        this.cargandoMesas = false;
      },
      error: (err) => {
        this.cargandoMesas = false;
        this.mesasAbiertas = [];
        console.error('Error listando mesas abiertas', err);
        this.errorLobby = 'No se pudieron cargar las mesas abiertas. Reiniciá el backend si acabás de actualizar.';
      }
    });
  }

  elegirJugador(ju: Jugador): void {
    const id = ju.id || '';
    if (!id) {
      return;
    }
    if (this.partidaLista) {
      return;
    }
    if (this.jugadores.includes(id)) {
      this.retirarRival(ju);
      return;
    }
    if (this.jugadores.length >= 6) {
      this.errorLobby = 'Máximo 6 jugadores por partida.';
      return;
    }
    this.errorLobby = '';
    this.jugadores.push(id);
    this.rivales.push(ju);
  }

  retirarRival(ju: Jugador): void {
    if (this.partidaLista) {
      return;
    }
    const id = ju.id || '';
    this.rivales = this.rivales.filter(rival => rival.id !== id);
    this.jugadores = this.jugadores.filter(jugador => jugador !== id);
  }

  crearPartidaNueva(): void {
    if (!this.puedeCrear) {
      this.errorLobby = this.totalEnMesa < 2
        ? 'Elegí al menos un rival para jugar.'
        : 'No se puede crear la partida ahora.';
      return;
    }

    this.creandoPartida = true;
    this.errorLobby = '';
    const dataTransfer: JugadorId[] = this.jugadores.map(id => ({ id }));

    this.partida.crearPartida(dataTransfer).subscribe({
      next: (data) => {
        this.linkPartida = this.baseUrl + data.id;
        this.idPartida = data.id;
        this.partidaEstado = data.estado || 'ESPERANDO';
        this.creandoPartida = false;
        if (data.id) {
          this.notifiedMesaIds.add(data.id);
        }
        this.copyLink();
        this.cargarMesasAbiertas();
      },
      error: (err) => {
        this.creandoPartida = false;
        console.error('Error creando partida', err);
        this.errorLobby = err?.error?.error || err?.message || 'No se pudo crear la partida';
      }
    });
  }

  entrarAlTablero() {
    if (!this.idPartida) {
      return;
    }
    this.router.navigate(['tablero', this.idPartida]);
  }

  iniciarYEntrar() {
    if (!this.idPartida || this.iniciandoPartida) {
      return;
    }
    this.iniciandoPartida = true;
    this.errorLobby = '';
    this.partida.iniciarPartida(this.idPartida, this.jugadorLog.id).subscribe({
      next: (data) => {
        this.iniciandoPartida = false;
        this.partidaEstado = data?.estado || 'EN_JUEGO';
        this.router.navigate(['tablero', this.idPartida]);
      },
      error: (err) => {
        this.iniciandoPartida = false;
        this.errorLobby = err?.error?.error || err?.message || 'No se pudo empezar la partida';
      }
    });
  }

  entrarMesa(mesa: MesaAbierta) {
    if (!mesa?.id) {
      return;
    }
    this.router.navigate(['tablero', mesa.id]);
  }

  iniciarMesa(mesa: MesaAbierta) {
    if (!mesa?.id || !this.puedeIniciarMesa(mesa) || this.iniciandoMesaId) {
      return;
    }
    this.iniciandoMesaId = mesa.id;
    this.errorLobby = '';
    this.partida.iniciarPartida(mesa.id, this.jugadorLog.id).subscribe({
      next: () => {
        this.iniciandoMesaId = '';
        this.router.navigate(['tablero', mesa.id]);
      },
      error: (err) => {
        this.iniciandoMesaId = '';
        this.errorLobby = err?.error?.error || err?.message || 'No se pudo empezar la partida';
      }
    });
  }

  cerrarMesa(mesa: MesaAbierta) {
    if (!mesa?.id || this.cerrandoMesaId) {
      return;
    }
    this.cerrandoMesaId = mesa.id;
    this.errorLobby = '';
    this.partida.cerrarPartida(mesa.id, this.jugadorLog?.id).subscribe({
      next: () => {
        this.cerrandoMesaId = '';
        if (this.idPartida === mesa.id) {
          this.linkPartida = '';
          this.idPartida = '';
          this.partidaEstado = '';
        }
        this.cargarMesasAbiertas();
      },
      error: (err) => {
        this.cerrandoMesaId = '';
        this.errorLobby = err?.error?.error || err?.message || 'No se pudo cerrar la mesa';
      }
    });
  }

  copyLink() {
    if (!this.linkPartida) {
      return;
    }
    this.clipboard.copy(this.linkPartida);
    this.linkCopiado = true;
    setTimeout(() => (this.linkCopiado = false), 2000);
  }

  nuevaSeleccion() {
    this.linkPartida = '';
    this.idPartida = '';
    this.partidaEstado = '';
    this.linkCopiado = false;
    this.todosJugadores();
  }

  private onLobbyEvent(event: LobbyRealtimeEvent): void {
    switch (event.type) {
      case 'PERFIL_UPDATED':
        this.applyPerfilUpdated(event);
        break;
      case 'MESA_CREADA':
        this.onMesaCreada(event);
        break;
      case 'MESA_CERRADA':
      case 'MESA_INICIADA':
        if (this.meInRoster(event)) {
          this.cargarMesasAbiertas();
        }
        break;
      default:
        break;
    }
  }

  private applyPerfilUpdated(event: LobbyRealtimeEvent): void {
    const id = event.jugadorId;
    if (!id) {
      return;
    }
    const patch = (j: Jugador): Jugador => ({
      ...j,
      nombre: event.nombre ?? j.nombre,
      avatar: event.avatar ?? j.avatar,
    });

    if (this.jugadorLog?.id === id) {
      this.jugadorLog = patch(this.jugadorLog);
      localStorage.setItem('jugador', JSON.stringify(this.jugadorLog));
    }

    this.usuarios = this.usuarios.map((u) => (u.id === id ? patch(u) : u));
    this.rivales = this.rivales.map((u) => (u.id === id ? patch(u) : u));
  }

  private onMesaCreada(event: LobbyRealtimeEvent): void {
    if (!this.meInRoster(event)) {
      return;
    }
    this.cargarMesasAbiertas();

    const partidaId = event.partidaId || '';
    const soyHost = !!this.jugadorLog?.id && event.hostJugadorId === this.jugadorLog.id;
    if (!partidaId || soyHost || this.notifiedMesaIds.has(partidaId)) {
      return;
    }
    this.notifiedMesaIds.add(partidaId);

    comicAlert({
      title: '¡Te invitaron!',
      html: '<div class="comic-swal-burst">VS</div><p class="comic-swal-sub">Hay una mesa nueva esperándote.</p>',
      confirmButtonText: 'Ver mesa',
      showCancelButton: true,
      cancelButtonText: 'Después',
    }).then((result) => {
      if (result.isConfirmed) {
        this.router.navigate(['tablero', partidaId]);
      }
    });
  }

  private meInRoster(event: LobbyRealtimeEvent): boolean {
    const myId = this.jugadorLog?.id;
    if (!myId) {
      return false;
    }
    if (event.hostJugadorId === myId) {
      return true;
    }
    return Array.isArray(event.jugadorIds) && event.jugadorIds.includes(myId);
  }
}
