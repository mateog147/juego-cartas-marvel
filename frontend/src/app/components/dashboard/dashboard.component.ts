import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Jugador } from 'src/app/interface/jugador';
import { AuthService } from 'src/app/shared/services/auth.service';
import { JugadorserviceService } from 'src/app/shared/services/jugadorservice.service';
import { PartidaService } from 'src/app/shared/services/partida.service';
import { Clipboard } from '@angular/cdk/clipboard';

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
export class DashboardComponent implements OnInit {
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

  constructor(
    public authService: AuthService,
    private partida: PartidaService,
    private router: Router,
    private jugador: JugadorserviceService,
    private clipboard: Clipboard
  ) {}

  ngOnInit(): void {
    this.todosJugadores();
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
}
