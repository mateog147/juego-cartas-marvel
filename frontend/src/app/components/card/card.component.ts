import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';

export interface Card{
  id?: string;
  nombre: string;
  imagen: string;
  xp: number;
}

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardComponent implements OnInit {

  @Input()
  xp!: number;
  @Input()
  nombre!: string;
  @Input() img!: string;

  

  constructor() { }

  ngOnInit(): void {
  }

  

}
