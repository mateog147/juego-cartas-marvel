import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-tablero',
  templateUrl: './tablero.component.html',
  styleUrls: ['./tablero.component.css']
})
export class TableroComponent implements OnInit {
  tablero: {status:boolean} = {status : false}
  constructor() { }

  ngOnInit(): void {
  }

}
