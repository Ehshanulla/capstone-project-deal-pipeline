import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/services/auth.service';
import { DealsService } from '../services/deal.sevice';
import { AdminServices } from '../../admin/services/admin-services';

@Component({
  selector: 'app-deal-create',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './deal-create.html',
  styleUrl: './deal-create.css',
})
export class DealCreate implements OnInit{
  role;
  loading = signal(false);
  error = signal<string | null>(null);
  dealForm;
  users: any[] = [];
  ngOnInit() {
    if (this.role === 'ADMIN') {
      this.loadUsers();
    }
  }

loadUsers() {
  this.admin.getUsers().subscribe({
    next: users => this.users = users.content,
    error: () => this.error.set('Failed to load users')
  });
}

  constructor(
    private fb: FormBuilder,
    private dealsService: DealsService,
    private router: Router,
    private auth: AuthService,
    private admin: AdminServices
  ) {
    this.role = this.auth.getRole();
  


  this.dealForm = this.fb.nonNullable.group({
    clientName: ['', Validators.required],
    dealType: ['', Validators.required],
    sector: ['', Validators.required],
    summary: [''],
      // ADMIN ONLY
    assignedTo: ['']
  });

}

  submit(): void {
    if (this.dealForm.invalid) {
      this.dealForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.dealsService.createDeal(this.dealForm.getRawValue())
      .subscribe({
        next: () => this.router.navigate(['/deals']),
        error: err => {
          this.error.set(err.error?.message ?? 'Failed to create deal');
          this.loading.set(false);
        }
      });
  }

  goBack() {
    this.router.navigate(['/deals']);
  }
}
