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
  linkPartida: string = ''
  baseUrl: string = 'localhost:4200/tablero/';
  idPartida: string = '';
  jugadores: string[] = [];
  usuarios: Jugador[] = [];
  rivales: Jugador[] = [];
  jugadorLog!: Jugador;
  constructor(public authService: AuthService,
    private partida: PartidaService,
    private router: Router,
    private jugador : JugadorserviceService,
    private clipboard: Clipboard) { }

  ngOnInit(): void {
    this.todosJugadores();

  }

  todosJugadores(){
    let user = JSON.parse(localStorage.getItem('jugador')!);
    this.jugadores.push(user.id);
    this.jugadorLog = user
    this.jugador.getJugadoresDB().subscribe((data) => {
      data.filter((jugador: Jugador) => jugador.id != user.id)
      .forEach((item: Jugador)=> this.usuarios.push(item))
      })
  }
  ngDoCheck(){

  }

  elegirJugador(ju : Jugador) : void {
    let id:string = ju.id ? ju.id : "";
    if(this.jugadores.includes(id) ){
     alert('Jugador ya agregado')
    }else if (this.jugadores.length > 5){
      alert('El juego ha llegado al limite de jugadores')
    }
    else{
      this.jugadores.push(id);
      this.rivales.push(ju);
    }
    console.log(this.jugadores, this.rivales);
    
  }

  retirarRival(ju : Jugador) : void {
    let id:string = ju.id ? ju.id : "";
    if(this.jugadores.includes(id)){
      this.rivales = this.rivales.filter(rival => rival.id !== id);
      this.jugadores = this.jugadores.filter(jugador => jugador !== id);
    }else{alert('Jugador no está agregado')}

  }

  crearPartidaNueva(): void {
    if(this.jugadores.length < 2){
      alert('No puedes jugar solo!');
    }else{    
    let dataTransfer: JugadorId[] = [];
    
    this.jugadores.forEach(jugador => {
      dataTransfer.push(({
        id: jugador
      }))
    });
   // this.linkPartida = 'link de la partida para compartir'
    //console.log(dataTransfer)
    this.partida.crearPartida(dataTransfer)
   .subscribe(data => {
    this.linkPartida = this.baseUrl + data.id ;
    this.idPartida = data.id});

  }}
 empezarPartida(){
  this.router.navigate(['tablero', this.idPartida])
 }
  
 copyLink(){
  
  this.clipboard.copy(this.linkPartida)
 }
}
