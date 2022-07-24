import { Component, DoCheck, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {FormBuilder} from '@angular/forms'
import {Card} from '../card/card.component'
import { AuthService } from 'src/app/shared/services/auth.service';
import { User } from 'src/app/shared/services/user';
import { JugadorserviceService } from 'src/app/shared/services/jugadorservice.service';
import { CartaserviceService } from 'src/app/shared/services/cartaservice.service';
import { ApuestaModel } from 'src/app/models/apuesta.model';
import { onLog } from 'firebase/app';
@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css']
})
export class TableroComponent implements OnInit , DoCheck {
  tablero: {status:boolean} = {status : false}
  apuestas: Card[] = [];
  mazo: Card[] = [];
  constructor(public authService: AuthService, 
            private jugadorService: JugadorserviceService, 
            private carta : CartaserviceService) {}
  
  ngDoCheck(): void {

    if(this.apuestas.length === 4){
      this.tablero.status = true;
    }
  }
 
 console = console;

 
  
  ngOnInit(): void {
    
    this.carta.getAll().subscribe(data=>{
      data.forEach((item: Card) =>this.mazo.push(item));
    })
    
  }
  drop(event: CdkDragDrop<Card[]>) {
    if (event.previousContainer === event.container ) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
      
    } else if((event.previousContainer === event.container) &&  event.container.data.length > 1 ) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex)
      console.log(event.container.id);
      console.log(event.container.data.length)
    }else{
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
        
      );}
      
      // toda la partida
      console.log(event.previousContainer.data);
      
      // la carta apostada
      console.log(event.container.id);
      
      this.newArray()
    }
  
  
  post(): void{
    console.log("hola");
   
   
  } 
  newArray(){
    localStorage.setItem('mazo', JSON.stringify(this.mazo));
    localStorage.setItem('apuesta', JSON.stringify(this.apuestas));

  }


  enviarApuesta(idPartida : string = "62dc5b92a90f4d384bc781d6"){
    const apuesta: ApuestaModel = {
      jugadorId: "62d86b3fc0428e3b4e3abe48",
      carta: {
        id: "62db504208cf801d22e295fa",
        nombre: "LA MOLE",
        xp: 3456212,
        imagen: "../../../assets/imgs/thing.jpg"
      }
    }     
    this.jugadorService.enviarApuesta(idPartida, apuesta).subscribe(item => console.log(item))
  }


  ganadorRonda(idPartida : string = "62dc5b92a90f4d384bc781d6"){
    this.jugadorService.ganadorRonda(idPartida).subscribe(item => console.log(item))
  }

}
