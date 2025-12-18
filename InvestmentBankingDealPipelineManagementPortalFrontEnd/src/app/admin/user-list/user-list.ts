import { Component, signal } from '@angular/core';
import { AdminServices, User } from '../services/admin-services';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-list',
  imports: [CommonModule, RouterModule],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList {
  users = signal<User[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  page = signal(0);
  size = signal(10);
  totalPages = signal(0);

  sortField = signal<'username' | 'email' | 'role' | 'createdAt'>('createdAt');
  sortDirection = signal<'asc' | 'desc'>('desc');

  constructor(
    private adminService: AdminServices,
    private router: Router
  ) {
    this.loadUsers();
  }

  loadUsers() {
    this.loading.set(true);

    this.adminService.getUsers(
      this.page(),
      this.size()
    ).subscribe({
      next: res => {
        this.users.set(res.content);
        this.totalPages.set(res.totalPages);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load users');
        this.loading.set(false);
      }
    });
  }

  nextPage() {
    if (this.page() + 1 < this.totalPages()) {
      this.page.set(this.page() + 1);
      this.loadUsers();
    }
  }

  prevPage() {
    if (this.page() > 0) {
      this.page.set(this.page() - 1);
      this.loadUsers();
    }
  }

  createUser() {
    this.router.navigate(['/admin/create']);
  }

  toggleStatus(user: User) {
    this.adminService.updateUserStatus(user.id, !user.active).subscribe({
      next: updated => {
        this.users.set(
          this.users().map(u => u.id === updated.id ? updated : u)
        );
      }
    });
  }
}
