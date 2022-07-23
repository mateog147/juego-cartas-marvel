import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { environment } from 'src/environments/environment';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireAuthModule } from '@angular/fire/compat/auth';
import { AngularFireStorageModule } from '@angular/fire/compat/storage';
import { AngularFirestoreModule } from '@angular/fire/compat/firestore';
import { AngularFireDatabaseModule } from '@angular/fire/compat/database';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {MatGridListModule} from '@angular/material/grid-list';
import { HttpClientModule } from '@angular/common/http';
import {MatTableModule} from '@angular/material/table';
import {MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';


import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

import { SignUpComponent } from './components/sign-up/sign-up.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { VerifyEmailComponent } from './components/verify-email/verify-email.component';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { AuthService } from './shared/services/auth.service';
import { RoutingModule } from './routing/routing.module';
import { CardComponent } from './components/card/card.component';
import { TableroComponent } from './components/tablero/tablero.component';
import { AvatarjugadorComponent } from './components/avatarjugador/avatarjugador.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TablacrudComponent } from './components/tablacrud/tablacrud.component';
import { JugadorserviceService } from './shared/services/jugadorservice.service';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    SignInComponent,
    SignUpComponent,
    ForgotPasswordComponent,
    VerifyEmailComponent,
    CardComponent,
    TableroComponent,
    AvatarjugadorComponent,
    TablacrudComponent,
    
  ],
  imports: [
    AngularFireModule.initializeApp(environment.firebase, 'sofkau-heroes'),
    AngularFireAuthModule,
    AngularFirestoreModule,
    AngularFireStorageModule,
    AngularFireDatabaseModule,
    BrowserModule,
    RoutingModule,
    DragDropModule,
    BrowserAnimationsModule,
    MatGridListModule,
    HttpClientModule,
    MatTableModule,
    MatFormFieldModule,
    MatPaginatorModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
   
    
  ],
  providers: [AuthService, JugadorserviceService],
  bootstrap: [AppComponent],
  exports: [
    CardComponent
  ],
})
export class AppModule {}
