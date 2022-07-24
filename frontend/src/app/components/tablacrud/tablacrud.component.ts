import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { FormBuilder } from '@angular/forms';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

import { CartaserviceService } from 'src/app/shared/services/cartaservice.service';
import { Card } from '../card/card.component';




@Component({
  selector: 'table-overview-example',
  styleUrls: ['tablacrud.component.css'],
  templateUrl: 'tablacrud.component.html',
})
export class TablacrudComponent implements AfterViewInit{
  displayedColumns: string[] = ['id','img', 'name', 'poder', 'boton'  ];
  dataSource!: Card[] ;
  form;
  formEdit : {id: string, status: boolean} = {id: '', status: false}
  images: string[] = ["azulydorado.jpg",
  "bishop.jpg",
  "bishop2.jpg",
  "bloodhawk.jpg",
  "boomboom.jpg",
  "buitrevsspider.jpg",
  "capitanamerica.jpg",
  "capvscap.jpg",
  "carnage.jpg",
  "carnage2.jpg",
  "centurious.jpg",
  "ciclope.jpg",
  "ciclopejoven.jpg",
  "conflictofinal.jpg",
  "congranpoder.jpg",
  "creaciondelarcangel.jpg",
  "daredevil.jpg",
  "demonioarania.jpg",
  "diosdeltrueno.jpg",
  "disfraznegro.jpg",
  "domino.jpg",
  "doom2099.jpg",
  "drdoom.jpg",
  "drpulpo.jpg",
  "duenderojo.jpg",
  "duendeverde.jpg",
  "elektra.jpg",
  "elnuevohulk.jpg",
  "elnuevonova.jpg",
  "enemigosmortales.jpg",
  "escorpionvsspidy.jpg",
  "exodus.jpg",
  "extranio.jpg",
  "factorrx.jpg",
  "fuerzax.jpg",
  "gambit.jpg",
  "gambito.jpg",
  "gatanegra.jpg",
  "ghost.jpg",
  "ghostrider.jpg",
  "havok.jpg",
  "hombrearania2099.jpg",
  "hombredeacero.jpg",
  "hulk.jpg",
  "hulk2099.jpg",
  "hulkjoven.jpg",
  "hulkvslamole.jpg",
  "hulkylospantheon.jpg",
  "iceman.jpg",
  "invasores.jpg",
  "ironman.jpg",
  "ironmanborn.jpg",
  "jeangrey.jpg",
  "jhonyblaze.jpg",
  "jubilo.jpg",
  "juiciomrfantastico.jpg",
  "laboda.jpg",
  "labrujaescarlata.jpg",
  "lallegadadegalactus.jpg",
  "lamole.jpg",
  "lapida.jpg",
  "larathos.jpg",
  "legado.jpg",
  "lobezno.jpg",
  "locuradevenom.jpg",
  "magnetoregresa.jpg",
  "maximumcarnage.jpg",
  "megaredvswolverine.jpg",
  "mephisto.jpg",
  "morbius.jpg",
  "mujerinvisible.jpg",
  "namor.jpg",
  "newghostrider.jpg",
  "nightcrawler.jpg",
  "psilord.jpg",
  "pumavsspidey.jpg",
  "punisher.jpg",
  "punisher2099.jpg",
  "quasar.jpg",
  "rhinovsspidy.jpg",
  "seguidoresdethor.jpg",
  "shehulk.jpg",
  "shriek.jpg",
  "silversableinc.jpg",
  "spider1.jpg",
  "spidercosmico.jpg",
  "spidervsduende.jpg",
  "spideyvolando.jpg",
  "srsiniestro.jpg",
  "strife.jpg",
  "thanos.jpg",
  "thanossentado.jpg",
  "thepunisher.jpg",
  "thing.jpg",
  "titania.jpg",
  "vengadores.jpg",
  "venganza.jpg",
  "venom-letal.jpg",
  "venom&edi.jpg",
  "venomup.jpg",
  "venomvive.jpg",
  "verdugosdearanias.jpg",
  "warlock.jpg",
  "warpath.jpg",
  "wolverinederrotado.jpg",
  "wolverinevshulk.jpg",
  "xavier.jpg",
  "xaviervsmagneto.jpg"];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
 
  constructor(private cardService: CartaserviceService,
    private formBuilder : FormBuilder) {
      
      this.form = this.formBuilder.group(
      {
        nombre : [''],
        xp: [''] ,
        imagen: ['']
      }
    )

  }
  ngAfterViewInit(): void {
    
    this.get()
    
  }
  

  
  get(){
    this.cardService.getAll().subscribe(data=> this.dataSource = data);
  }
  
  
  onSubmit(){
    
    const dataCarta  ={
        
        nombre: this.form.value.nombre!,
        imagen:`../../../assets/imgs/${this.form.value.imagen!}`,
        xp: Number(this.form.value.xp) 
    }
    this.formEdit.status?
    this.cardService.editCarta( this.formEdit.id, dataCarta).subscribe({next: (item: Card) => {console.log(item)}, error: (e)=>{console.log(e)}})
     :
    this.cardService.crearNuevaCarta(dataCarta).subscribe({next: (item: Card) => {console.log(item)}, error: (e)=>{console.log(e)}});
    this.formEdit.status = false;
    this.get();
    this.form.reset();
    
    
  }

  onEdit(idC: string, name: string, img: string, xp: string){
    this.formEdit = {
      id: idC,
      status: true
    }
    this.form = this.formBuilder.group(
      {
        nombre : [name],
        xp: [xp] ,
        imagen: [img]
      })
    
   console.log(this.formEdit);
    
  }

  onDelete(idC: string){
    this.cardService.eliminarCarta(idC).subscribe(data => {console.log(data)})
    this.cardService.getAll().subscribe(data => {this.dataSource = data})
  }

}

