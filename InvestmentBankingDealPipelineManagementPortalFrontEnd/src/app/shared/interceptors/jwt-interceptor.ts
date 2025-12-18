import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = localStorage.getItem('jwt');

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError(error => {

      const code = error?.error?.code;

      // 🔐 Token expired or invalid
      if (error.status === 401) {
        authService.logout();
        router.navigate(['/login']);
      }

      // 🚫 User deactivated
      if (code === 'USER_INACTIVE') {
        authService.logout();
        router.navigate(['/login']);
      }

      // 🚫 Access denied (403) - silent handling, no alert
        if (error.status === 403) {
          // Optionally, you can log it or navigate somewhere
          console.warn('Access denied:', error.error?.message);
          // router.navigate(['/unauthorized']); // optional
        }

        return throwError(() => error);
      })
     );
};
