import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

export interface Card{
  nombre: string;
  image: string;
  description: string;
  xp: number;
}
@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardComponent implements OnInit {

   card: Card = {nombre: 'Tribunal Viviente', image: '../../../assets/maxresdefault.jpg', description:'El mejor de todos', xp: 99999999}

  constructor() { }

  ngOnInit(): void {
  }

  

}
