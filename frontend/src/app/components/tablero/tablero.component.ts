import { Component, DoCheck, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Card} from '../card/card.component'
import { AuthService } from 'src/app/shared/services/auth.service';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';
import { PartidaService } from 'src/app/shared/services/partida.service';
import { Jugador } from 'src/app/interface/jugador';

@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css']
})

export class TableroComponent implements OnInit , DoCheck {
  
  tablero: {status:boolean} = {status : false}
  
  partidaId = "62dc5b92a90f4d384bc781d6";

  apuestas: Card[] = [];
  mazo: Card[] = [];
  
  partida:any;
  jugadoruid: any;
  jugadorInfo: any;

  constructor(public authService: AuthService, 
            private partidaService: PartidaService) {}
  
  ngDoCheck(): void {
    if(this.apuestas.length === 3){
      this.tablero.status = true;
    }
  }
  
  ngOnInit(): void {
    
    this.getPartidaPorId(this.partidaId);

  }

  drop(event: CdkDragDrop<Card[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);

    } else {

      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
        
      );

      this.newArray()

      // toda la partida
      console.log(event.previousContainer.data);

      let cartaApostada: Card = event.container.data[0]
      this.enviarApuesta(this.partidaId , cartaApostada)

    }
  }
  
  post(): void{
    console.log("hola");
  } 

  newArray(){
    localStorage.setItem('mazo', JSON.stringify(this.mazo));
    localStorage.setItem('apuesta', JSON.stringify(this.apuestas));

  }

  imprimir(){
    this.jugadoruid = this.authService.userData.uid;   
    this.getJugadorInfo();
    this.getMazo();
    console.log(this.jugadorInfo);
  }

  getPartidaPorId(partidaId : string){
    this.partidaService.getPartidaporId(partidaId)
    .subscribe(item => this.partida = item)
  }

  //TODO: cambiar "xxx" por this.jugadoruid
  getJugadorInfo(){
    this.partida.jugadores.forEach((jugador: Jugador) => {
      if(jugador.uid == "xxx" ){ this.jugadorInfo = jugador} 
    })
  }

  getMazo(){
    this.mazo = this.jugadorInfo.cartas;
  }

  enviarApuesta(partidaId : string , carta: Card){
  
    const apuesta: ApuestaModel = {
      jugadorId: this.jugadorInfo.id,
      carta: {
        id: carta.id,
        nombre: carta.nombre,
        xp: carta.xp,
        imagen: carta.imagen
      }
    }     
    this.partidaService.enviarApuesta(partidaId, apuesta).subscribe(item => console.log(item))
  }

  ganadorRonda(idPartida : string = this.partidaId){
    this.partidaService.ganadorRonda(idPartida).subscribe(item => console.log(item))
  }

}
