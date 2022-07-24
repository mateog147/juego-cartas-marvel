import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService){}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean{
       let user = JSON.parse(localStorage.getItem('user')!)
      if( user.uid !== "KzO23QhzobSiYFjVf1Fz3kxLGUH3"  ) {
        this.router.navigate(['dashboard'])
        alert("no estas autorizado")
      }
    return true;
  }
  
}
