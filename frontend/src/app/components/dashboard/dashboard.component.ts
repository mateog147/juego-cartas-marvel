import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Jugador } from 'src/app/interface/jugador';
import { AuthService } from 'src/app/shared/services/auth.service';
import { JugadorserviceService } from 'src/app/shared/services/jugadorservice.service';
import { PartidaService } from 'src/app/shared/services/partida.service';

export interface JugadorId {
  id: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {
  linkPartida: string = ''
  jugadores: string[] = [];
  usuarios: Jugador[] = [];
  rivales: Jugador[] = [];
  constructor(public authService: AuthService,
    private partida: PartidaService,
    private router: Router,
    private jugador : JugadorserviceService) { }

  ngOnInit(): void {
    this.todosJugadores();

  }

  todosJugadores(){
    let userId = JSON.parse(localStorage.getItem('jugador')!).id;
    this.jugadores.push(userId);
    this.jugador.getJugadoresDB().subscribe((data) => {
      data.filter((jugador: Jugador) => jugador.id != userId)
      .forEach((user: Jugador)=> this.usuarios.push(user))
      })
  }
  ngDoCheck(){

  }

  elegirJugador(ju : Jugador) : void {
    let id:string = ju.id ? ju.id : "";
    if(!this.jugadores.includes(id)){
     this.jugadores.push(id);
     this.rivales.push(ju);
    }else{alert('Jugador ya agregado')}

  }

  retirarRival(ju : Jugador) : void {
    let id:string = ju.id ? ju.id : "";
    if(this.jugadores.includes(id)){
      this.rivales = this.rivales.filter(rival => rival.id !== id);
      this.jugadores = this.jugadores.filter(jugador => jugador !== id);
    }else{alert('Jugador no está agregado')}

  }

  crearPartidaNueva(): void {
    let dataTransfer: JugadorId[] = [];

    this.jugadores.forEach(jugador => {
      dataTransfer.push(({
        id: jugador
      }))
    });

    //console.log(dataTransfer)
    this.partida.crearPartida(dataTransfer)
    .subscribe(data => this.router.navigate(['tablero', data.id]));

  }


}
