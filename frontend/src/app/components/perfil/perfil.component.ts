import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Jugador } from 'src/app/interface/jugador';
import { AuthService } from 'src/app/shared/services/auth.service';
import { AVATAR_PRESETS, resolveAvatar } from 'src/app/shared/utils/avatars';
import { comicAlert, comicError } from 'src/app/shared/utils/comic-alert';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  jugador!: Jugador;
  nombre = '';
  avatar = AVATAR_PRESETS[0];
  presets = AVATAR_PRESETS;
  guardando = false;
  email = '';

  constructor(
    public authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('jugador');
    if (!raw) {
      this.router.navigate(['/sign-in']);
      return;
    }
    this.jugador = JSON.parse(raw);
    this.nombre = this.jugador.nombre || this.authService.userData?.displayName || '';
    this.avatar = resolveAvatar(this.jugador.avatar, this.authService.userData?.photoURL);
    this.email = this.authService.userData?.email || '';
  }

  elegirAvatar(path: string) {
    this.avatar = path;
  }

  get puedeGuardar(): boolean {
    const n = (this.nombre || '').trim();
    return !this.guardando && n.length >= 1 && n.length <= 24;
  }

  guardar() {
    if (!this.puedeGuardar) {
      return;
    }
    this.guardando = true;
    this.authService.actualizarPerfilLocal(this.nombre.trim(), this.avatar)
      .then((data) => {
        this.guardando = false;
        this.jugador = data;
        comicAlert({
          title: '¡Perfil listo!',
          html: '<div class="comic-swal-burst">OK</div><p class="comic-swal-sub">Nombre y avatar guardados.</p>',
          confirmButtonText: 'Al lobby',
        }).then(() => this.router.navigate(['/dashboard']));
      })
      .catch((err) => {
        this.guardando = false;
        comicError(err?.error?.error || err?.message || 'No se pudo guardar el perfil');
      });
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}
