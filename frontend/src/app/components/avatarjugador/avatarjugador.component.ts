import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-avatarjugador',
  templateUrl: './avatarjugador.component.html',
  styleUrls: ['./avatarjugador.component.css']
})
export class AvatarjugadorComponent implements OnInit {
  @Input() imagen !: string;
  @Input()
  nombre!: string;
  constructor() { }

  ngOnInit(): void {
  }

}
