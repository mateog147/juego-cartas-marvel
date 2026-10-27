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

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  linkPartida = '';
  baseUrl = `${window.location.origin}/tablero/`;
  idPartida = '';
  jugadores: string[] = [];
  usuarios: Jugador[] = [];
  rivales: Jugador[] = [];
  jugadorLog!: Jugador;
  cargandoJugadores = true;
  creandoPartida = false;
  linkCopiado = false;
  errorLobby = '';

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

  etiquetaJugador(j: Jugador): string {
    const nombre = j.nombre || 'Jugador';
    const shortUid = (j.uid || j.id || '').slice(-4).toUpperCase();
    return shortUid ? `${nombre} · #${shortUid}` : nombre;
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
        this.creandoPartida = false;
        this.copyLink();
      },
      error: (err) => {
        this.creandoPartida = false;
        console.error('Error creando partida', err);
        this.errorLobby = err?.error?.error || err?.message || 'No se pudo crear la partida';
      }
    });
  }

  empezarPartida() {
    if (!this.idPartida) {
      return;
    }
    this.router.navigate(['tablero', this.idPartida]);
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
    this.linkCopiado = false;
    this.todosJugadores();
  }
}
