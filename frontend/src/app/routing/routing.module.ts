import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';
import { SignUpComponent } from '../components/sign-up/sign-up.component';
import { DashboardComponent } from '../components/dashboard/dashboard.component';
import { ForgotPasswordComponent } from '../components/forgot-password/forgot-password.component';
import { VerifyEmailComponent } from '../components/verify-email/verify-email.component';
import { SignInComponent } from '../components/sign-in/sign-in.component';
import { AuthGuard } from '../shared/guard/auth.guard';
import { TableroComponent } from '../components/tablero/tablero.component';
import { TablacrudComponent } from '../components/tablacrud/tablacrud.component';
import { AdminGuard } from '../shared/guard/admin.guard';

const routes: Routes = [
  { path: '', redirectTo: '/sign-in', pathMatch: 'full' },
  { path: 'sign-in', component: SignInComponent },
  { path: 'register-user', component: SignUpComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'verify-email-address', component: VerifyEmailComponent },
  {path: 'tablero/:idPartida', component: TableroComponent, canActivate: [AuthGuard] },
  {path: 'crud', component: TablacrudComponent, canActivate: [AuthGuard, AdminGuard], },

];

@NgModule({


  imports: [RouterModule.forRoot(routes),
  ],
  exports: [RouterModule],

})
export class RoutingModule {




}
