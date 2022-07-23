import {AfterViewInit, Component, ViewChild} from '@angular/core';
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

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private cardService: CartaserviceService) {
    
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
}


