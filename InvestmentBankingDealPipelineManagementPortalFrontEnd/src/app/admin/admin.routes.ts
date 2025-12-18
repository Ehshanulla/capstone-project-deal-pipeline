import { Routes } from '@angular/router';
import { UserCreate } from './user-create/user-create';
import { UserList } from './user-list/user-list';

export const ADMIN_ROUTES: Routes = [
    { path: '', component: UserList },
    { path: 'create', component: UserCreate },
];