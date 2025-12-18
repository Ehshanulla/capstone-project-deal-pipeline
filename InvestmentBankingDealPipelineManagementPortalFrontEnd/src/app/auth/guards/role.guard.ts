import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, UrlTree} from '@angular/router';
import { AuthService } from '../services/auth.service';


@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
    constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const expectedRole = route.data['role'];
    const actualRole = this.auth.getRole();

    // ✅ Authorized
    if (actualRole === expectedRole) {
      return true;
    }

    // 🔁 Redirect based on actual role
    return this.router.createUrlTree([
      actualRole === 'ADMIN' ? '/admin' : '/deals'
    ]);
  }


}
