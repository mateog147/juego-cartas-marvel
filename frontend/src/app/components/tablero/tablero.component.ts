import { Component, DoCheck, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {FormBuilder} from '@angular/forms'
import {Card} from '../card/card.component'
import { AuthService } from 'src/app/shared/services/auth.service';
import { User } from 'src/app/shared/services/user';
import { JugadorserviceService } from 'src/app/shared/services/jugadorservice.service';
import { CartaserviceService } from 'src/app/shared/services/cartaservice.service';
import { Jugador } from 'src/app/shared/services/jugador';
@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css']
})
export class TableroComponent implements OnInit , DoCheck {
  tablero: {status:boolean} = {status : false}
  apuestas: Card[] = [];
  mazo: Card[] = [];
  jugadores : any [] = [2, 2, 2];
  
  constructor(
    public authService: AuthService, 
    private jugador: JugadorserviceService, 
    private carta : CartaserviceService , 
    ) {
      
    }


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
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
      
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
        
      );this.newArray()
    }
  }
  
  post(): void{
    console.log("hola");
   
   
  } 
  newArray(){
    localStorage.setItem('mazo', JSON.stringify(this.mazo));
    localStorage.setItem('apuesta', JSON.stringify(this.apuestas));

  }
 
}
