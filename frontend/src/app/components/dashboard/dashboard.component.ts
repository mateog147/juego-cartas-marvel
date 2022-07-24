import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Jugador } from 'src/app/interface/jugador';
import { AuthService } from 'src/app/shared/services/auth.service';
import { JugadorserviceService } from 'src/app/shared/services/jugadorservice.service';
import { PartidaService } from 'src/app/shared/services/partida.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  linkPartida: string = 'http://localhost:4200/'
  jugadores: string[] = [];
  usuarios!: Jugador[];
  constructor(public authService: AuthService, private partida: PartidaService, private router: Router, private jugador : JugadorserviceService) { }
  
  ngOnInit(): void {
    this.todosJugadores()
  }

  todosJugadores(){
    this.jugador.getJugadoresDB().subscribe((data) => this.usuarios = data)
  }

  elegirJugador(ju : string) : void {
    if(!this.jugadores.includes(ju)){
     this.jugadores.push(ju);
    }else{alert('Jugador ya agregado')}
    
    console.log(ju);
    console.log(this.jugadores);
    
    
  }
  
  crearPartidaNueva(): void {
     
    this.partida.crearPartida(this.jugadores)
    .subscribe(data => this.linkPartida.concat(data.id));
    //this.router.navigate(['tablero'])
    
  }

  

}
