import { Routes } from '@angular/router';
import { DealList } from './deal-list/deal-list';
import { DealCreate } from './deal-create/deal-create';
import { DealEdit } from './deal-edit/deal-edit';
import { AuthGuard } from '../auth/guards/auth.guard';
import { Kanban } from './kanban/kanban';

export const DEALS_ROUTES: Routes = [
    {path: '',component: DealList,canActivate: [AuthGuard]},
    {path: 'create',component: DealCreate,canActivate: [AuthGuard]},
    {path: 'edit/:id',component: DealEdit,canActivate: [AuthGuard]},
    {path: 'kanban',component: Kanban,canActivate: [AuthGuard]}

];