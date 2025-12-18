// app.routes.ts
import { Routes } from '@angular/router';
import { AuthGuard } from './auth/guards/auth.guard';
import { RoleGuard } from './auth/guards/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },

  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.routes').then(r => r.AUTH_ROUTES)
  },
  {
    path: 'deals',
    loadChildren: () => import('./deals/deals.routes').then(r => r.DEALS_ROUTES),
    canActivate: [AuthGuard]
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.routes').then(r => r.ADMIN_ROUTES),
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  }
];

