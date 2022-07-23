import {AfterViewInit, Component, ViewChild} from '@angular/core';
import { FormBuilder } from '@angular/forms';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { CartaserviceService } from 'src/app/shared/services/cartaservice.service';
import { Card } from '../card/card.component';




@Component({
  selector: 'table-overview-example',
  styleUrls: ['tablacrud.component.css'],
  templateUrl: 'tablacrud.component.html',
})
export class TablacrudComponent implements AfterViewInit {
  displayedColumns: string[] = ['id','fruit', 'name', 'progress', 'boton'  ];
  dataSource: MatTableDataSource<Card> ;
  form;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
 
  constructor(private cardService: CartaserviceService,
    private formBuilder : FormBuilder) {
      
      this.form = this.formBuilder.group(
      {
        nombre : [''],
        xp: [''],
        imagen: ['']
      }
    )
    
    const dataCar = new Array;
    this.cardService.getAll().subscribe(item => item.forEach((e: any) => dataCar.push(e)));

   
    this.dataSource = new MatTableDataSource(dataCar)
    

  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  onSubmit(){
    console.log(this.form.value);
  }
}


