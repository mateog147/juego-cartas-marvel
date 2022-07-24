import { AfterViewChecked, AfterViewInit, Component, DoCheck, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

import {Card} from '../card/card.component'
import { AuthService } from 'src/app/shared/services/auth.service';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';
import { PartidaService } from 'src/app/shared/services/partida.service';
import { Jugador } from 'src/app/interface/jugador';
import { ActivatedRoute, Params } from '@angular/router';
@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css']
})

export class TableroComponent implements AfterViewChecked, AfterViewInit ,OnInit , DoCheck {
  
  tablero: {status:boolean} = {status : false}
  
  partidaId !: string;

  apuestas: Card[] = [];
  mazo: Card[] = [];

  partida:any;
  jugadoruid: any;
  jugadorInfo: any;

  constructor(public authService: AuthService, 
            private partidaService: PartidaService, 
            private rutaActiva : ActivatedRoute) {}
  
  
  ngDoCheck(): void {
    if(this.apuestas.length === 3){
      this.tablero.status = true;
    }
  }

  ngOnInit(): void {
     this.partidaId = this.rutaActiva.snapshot.paramMap.get('idPartida')!;
     this.jugadoruid = JSON.parse(localStorage.getItem('user')!).uid;
     this.getPartidaPorId(this.partidaId);
  }

  ngAfterViewInit(){
    
    
  }
  ngAfterViewChecked(){
    //this.imprimir()
  }

  ngOnChanges() {

    //if(changes.mazo.currentValue != changes.partida.previousValue){
      
    this.getPartidaPorId(this.partidaId);
    this.imprimir();
    console.log("algo cambio");
    //} 

    
    
  }

  drop(event: CdkDragDrop<Card[]>) {
    if (event.previousContainer === event.container ) {
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

      console.log(cartaApostada);
      
      this.enviarApuesta(this.partidaId , cartaApostada)

    }}

  
  
  
  
 

  newArray(){
    localStorage.setItem('mazo', JSON.stringify(this.mazo));
    localStorage.setItem('apuesta', JSON.stringify(this.apuestas));

  }

  imprimir(){
          
   this.getJugadorInfo();
   this.getMazo();
    console.log(this.jugadoruid)
    console.log(this.partida);
    console.log(JSON.parse(localStorage.getItem('user')!).uid)
  }

  getPartidaPorId(partidaId : string){
    this.partidaService.getPartidaporId(partidaId)
    .subscribe(item => {
      this.partida = item;
      this.imprimir()})
  }

  
   getJugadorInfo()  { 
   
    
    this.partida.jugadores.forEach((jugador: Jugador) => {
      if(jugador.uid === this.jugadoruid){ this.jugadorInfo = jugador
      console.log(jugador);
      } 
    })
  }

  getMazo(){
    this.mazo = this.jugadorInfo.cartas
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
    this.partidaService.ganadorRonda(idPartida).subscribe(item => console.log(item));
     if(this.jugadorInfo.cartas.length === 0){ 
      alert("Has perdido noob")
     }
  }

}
