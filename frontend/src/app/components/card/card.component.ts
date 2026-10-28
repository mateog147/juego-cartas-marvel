import { ChangeDetectionStrategy, Component, Input, OnChanges } from '@angular/core';
import { resolveCardArt } from 'src/app/shared/utils/card-art';

export interface Card {
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
export class CardComponent implements OnChanges {

  @Input() xp!: number;
  @Input() nombre!: string;
  @Input() img!: string;

  artUrl = 'assets/imgs/boomboom.jpg';

  ngOnChanges(): void {
    this.artUrl = resolveCardArt(this.nombre, this.img);
  }
}
