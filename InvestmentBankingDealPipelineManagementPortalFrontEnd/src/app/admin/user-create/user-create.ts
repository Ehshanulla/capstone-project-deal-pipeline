import { Component, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { User, AdminServices } from '../services/admin-services';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-user-create',
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './user-create.html',
  styleUrl: './user-create.css',
})
export class UserCreate {
  form;
 loading = signal(false);
  error = signal<string | null>(null);
  fieldErrors = signal<Record<string, string> | null>(null);

 
  constructor(
    private fb: FormBuilder,
    private adminService: AdminServices,
    private router: Router
  ) {
     this.form = this.fb.group({
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
    role: ['USER', Validators.required]
  });

  }

  submit() {
    if (this.form.invalid) return;

    this.loading.set(true);
    this.error.set(null);
    this.fieldErrors.set(null);

    this.adminService.createUser(this.form.value).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/admin']);
      },

      error: (err) => {
        this.loading.set(false);

        const apiError = err?.error;

        // ✅ Validation errors (field level)
        if (apiError?.code === 'VALIDATION_ERROR') {
          this.fieldErrors.set(apiError.details);
          return;
        }

        // ✅ Business / permission errors
        this.error.set(apiError?.message ?? 'Something went wrong');
      }
    });
  }

  goBack(){
    this.router.navigate(['/admin']);
  }
}
