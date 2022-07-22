import { Component, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Card} from '../card/card.component'
import { AuthService } from 'src/app/shared/services/auth.service';
import { User } from 'src/app/shared/services/user';
import { JugadorserviceService } from 'src/app/shared/services/jugadorservice.service';
@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css']
})
export class TableroComponent implements OnInit {
  tablero: {status:boolean} = {status : true}
  apuestas: Card[] = [{nombre: 'Viviente', image: '../../../assets/maxresdefault.jpg', description:'El mejor de todos', xp: 99999999},{nombre: 'Tribunal Viviente', image: '../../../assets/maxresdefault.jpg', description:'El mejor de todos', xp: 99999999},{nombre: 'Tribunal Viviente', image: '../../../assets/maxresdefault.jpg', description:'El mejor de todos', xp: 99999999}];
  mazo: Card[] = [{nombre: 'Viviente', image: '../../../assets/maxresdefault.jpg', description:'El mejor de todos', xp: 99999999},{nombre: 'Tribunal Viviente', image: '../../../assets/maxresdefault.jpg', description:'El mejor de todos', xp: 99999999},{nombre: 'Tribunal Viviente', image: '../../../assets/maxresdefault.jpg', description:'El mejor de todos', xp: 99999999}]
  constructor(public authService: AuthService, private jugador: JugadorserviceService) {}
 
 console = console;

  
  
 
  
  ngOnInit(): void {
    
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
    this.jugador.nuevoJugador(this.authService.userData.uid, this.authService.userData.displayName)
    .subscribe(data => console.log(data));
  } 
  newArray(){
    localStorage.setItem('mazo', JSON.stringify(this.mazo));
    localStorage.setItem('apuesta', JSON.stringify(this.apuestas));

  }
 
}
