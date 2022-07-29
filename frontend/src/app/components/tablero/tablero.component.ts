import { AfterViewChecked, AfterViewInit, ChangeDetectionStrategy, Component, DoCheck, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

import {Card} from '../card/card.component'
import { AuthService } from 'src/app/shared/services/auth.service';
import { ApuestaModel } from 'src/app/interface/apuesta.interface';
import { PartidaService } from 'src/app/shared/services/partida.service';

import { Jugador } from 'src/app/interface/jugador';
import { ActivatedRoute, Params } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import {map, takeWhile} from 'rxjs/operators';
import { add } from 'date-fns';
import Swal from 'sweetalert2';
import { WebsocketService } from 'src/app/shared/service/websocket.service';

@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css'],
  //changeDetection: ChangeDetectionStrategy.OnPush
})


export class TableroComponent implements OnInit , DoCheck {

  tablero: {status:boolean} = {status : false}
  partidaId !: string;
  ronda: number = 0;
  apuestas!: {jugadorId: string, carta:Card}[] ;
  mazo: Card[] = [];
  apuestadrop: Card[] = [];
  jugadores: any;
  partida:any;
  jugadoruid: any;
  jugadorInfo: any;
  timeInterval : number = 1000;
  time!: number;
  subscripcion!: Subscription;
  constructor(public authService: AuthService, 
            private partidaService: PartidaService, 
            private rutaActiva : ActivatedRoute,
            private websocket: WebsocketService,
           
            ) {   

              // let socket = new WebSocket('ws://localhost:8080/ws/apuestas');
               let socket2 = new WebSocket('ws://localhost:8080/ws/ganadores');
           
              // socket.addEventListener('message', (event: MessageEvent) => {
              //   console.log((event.data )+ 'apuesta hecha');
                
              // })
               socket2.addEventListener('message', (event: MessageEvent) => {
               let ganador = JSON.parse(event.data);
               
               if(ganador.id === this.partida.ronda.id){
                Swal.fire({timer: 5000, timerProgressBar: true, html: `<h2>El ganador de la ronda: ${ganador.ultimoGanador} </h2></hr>
                <span style='font-size:100px;'>&#128526;</span>`}).then(result => {
                  if(result.isConfirmed || result.dismiss === Swal.DismissReason.timer){
                    this.getPartidaPorId();
                    this.time = this.partida.ronda.isTimerOn;
                  }
                })}})
                 
                
                
               
            
              this.websocket.messages.subscribe(message => {
                message.id === this.partida.ronda.id ?
                this.apuestas = message.apuestas :

                ''               
              })
            }

 
  ngDoCheck(): void {
    //this.partidaService.getPartidaporId(this.partidaId).subscribe((partida : any)=> {
      
    if(this.apuestas.length === this.partida.jugadores.length) {
      this.tablero.status = true;
      //console.log(this.apuestas);
      
    }
    else{ this.tablero.status = false}
  }
  

  ngOnInit(): void {
     this.partidaId = this.rutaActiva.snapshot.paramMap.get('idPartida')!;
     this.jugadoruid = JSON.parse(localStorage.getItem('user')!).uid;
     this.getPartidaPorId();
     
     
     this.subscripcion = this.partidaService.getRefresh$().subscribe(
     //()=> this.ganadorRonda()
     ) 
     
     
     
  }

  ngAfterViewInit(){
    //this.websocket.readMsg();
    this.partidaService.getPartidaporId(this.partidaId)
    .subscribe((data : any) => {
      data.jugadores.length === 1 ?   
      Swal.fire(`<h2>El ganador del juego fue: ${this.partida.ronda.ultimoGanador} </h2><hr/>
      <span style='font-size:100px;'>&#129321;</span>`) :
  
      this.jugadores = data.jugadores;
      this.time = data.ronda.isTimerOn;
      this.onTime();
    })
        
    
   // console.log(this.partida)
    
  
  }
  ngAfterViewChecked(){
    //this.onTime()
  }

  ngOnChanges() {

    //if(changes.mazo.currentValue != changes.partida.previousValue){

    // this.getPartidaPorId();
    // this.imprimir();
    // console.log("algo cambio");
    

  }
  

  drop(event: CdkDragDrop<Card[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);

    // } else if (this.apuestas.filter(item =>
    //   item.jugadorId === this.jugadorInfo.id).length > 0 ) {
    //     moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    //     Swal.fire(`Ya hiciste tu apuesta, espera a los demas jugadores`);
    }else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,)

      ;

      this.newArray()

      let cartaApostada: Card = event.container.data[0]

     // console.log(cartaApostada);

      this.enviarApuesta(this.partidaId , cartaApostada);
      this.renderTableroApuestas()
      //window.location.reload();
    }}

    
   
  onTime(){
    
    let fina = 12000;
    interval(1000).pipe(
        takeWhile(() => fina -- > 0))
        .subscribe({next: () => {
          this.time --;
         if( this.time == 0 ) {this.tomarCartaRandom();
          //this.ganadorRonda()
        }
      
      }})
    
    
    
  }
  //!TOMAR CARTA AL FINALIZAR RELOJ
  tomarCartaRandom(){
    if(this.mazo.length > 0 && this.filtrarJugadorEnapuesta().length === 0 ){
    this.enviarApuesta(this.partidaId, this.mazo[0])} 
   
   
   
  }

  //!PREGUNTAR SI HAY APUESTA DEL JUGADOR
  filtrarJugadorEnapuesta(){
    return this.apuestas.filter(item =>
      item.jugadorId === this.jugadorInfo.id)
  }
  
  //!INFO DE LOS MAZOS
  newArray(){
    localStorage.setItem('mazo', JSON.stringify(this.mazo));
    localStorage.setItem('apuestadrop', JSON.stringify(this.apuestadrop));

  }

  imprimir(){
   this.renderTableroApuestas();
   this.getJugadorInfo();
   this.getMazo();
   
    //console.log(this.jugadoruid)
    //console.log(this.partida);
    //console.log(this.jugadores);
    
    //console.log(JSON.parse(localStorage.getItem('user')!).uid)
  }

  getPartidaPorId() {
    this.partidaService.getPartidaporId(this.partidaId)
    .subscribe(item => {
      this.partida = item;
      
      this.imprimir()})
      
  }

  renderTableroApuestas(){
    this.partida.ronda.apuestas.length > 0 ?
    this.apuestas = this.partida.ronda.apuestas :
    this.apuestas = [];
    this.ronda = this.partida.ronda.numero;

  }
   getJugadorInfo()  {


    this.partida.jugadores.forEach((jugador: Jugador) => {
      if(jugador.uid === this.jugadoruid){ this.jugadorInfo = jugador
      //console.log(jugador);
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
    
    //this.websocket.messages.next(apuesta)
    //this.websocket.send('apuesta')
    this.partidaService.enviarApuesta(partidaId, apuesta).subscribe()
  }

  ganadorRonda(idPartida : string = this.partidaId){
    this.tablero.status ? 
    this.partidaService.ganadorRonda(idPartida).subscribe(item => 
    Swal.fire({timer: 10000, timerProgressBar: true, html: `<h2>El ganador de la ronda: ${item.ronda.ultimoGanador} </h2></hr>
    <span style='font-size:100px;'>&#128526;</span>`}).then(result => {
      if(result.isConfirmed || (result.dismiss === Swal.DismissReason.timer)){
        this.getPartidaPorId();
        this.time = this.partida.ronda.isTimerOn;
      }
    })):
    this.getPartidaPorId()
     
    if(this.mazo.length === 0){
      alert("Has perdido noob")
     }

    
  }


}