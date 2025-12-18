import { Component, signal, effect } from '@angular/core';
import { AuthService } from '../../auth/services/auth.service';
import { Deal, DealsService } from '../services/deal.sevice';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-deal-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './deal-list.html',
  styleUrls: ['./deal-list.css'],
})
export class DealList {

  // ---------------- Signals ----------------
  deals = signal<Deal[]>([]);
  page = signal(0);
  size = signal(10);
  totalPages = signal(0);

  loading = signal(true);

  sortField = signal('createdAt');
  sortDirection = signal<'asc' | 'desc'>('desc');

  role: string | null;

  constructor(
    private dealsService: DealsService,
    private auth: AuthService
  ) {
    this.role = this.auth.getRole();

  effect(() => {
    const page = this.page();
    const size = this.size();
    const sortField = this.sortField();
    const sortDirection = this.sortDirection();

    this.loadDeals(page, size, sortField, sortDirection);
  });


  }

  searchTerm = '';
  allDeals: any[] = []; // original data
  filteredDeals = signal<any[]>([]);


  loadDeals(
    page: number,
    size: number,
    sortField: string,
    sortDirection: 'asc' | 'desc'
  ): void {
    const sort = `${sortField},${sortDirection}`;

    this.dealsService.getDeals(page, size, sortField, sortDirection)
      .subscribe(res => {
        this.deals.set(res.content);
        this.filteredDeals.set(res.content);
        this.loading.set(false)
        this.totalPages.set(res.totalPages);
        this.page.set(res.page);
        this.size.set(res.size);
      });

  }

    // ---------------- Search ----------------
  applyFilter(): void {
    const term = this.searchTerm.toLowerCase().trim();

    if (!term) {
      this.filteredDeals.set(this.deals());
      return;
    }

    this.filteredDeals.set(
      this.deals().filter(deal =>
        deal.clientName.toLowerCase().includes(term) ||
        deal.dealType.toLowerCase().includes(term) ||
        deal.sector.toLowerCase().includes(term)
      )
    );
  }


  // ---------------- Sorting ----------------
  sortBy(field: string): void {
    if (this.sortField() === field) {
      // toggle direction
      this.sortDirection.set(
        this.sortDirection() === 'asc' ? 'desc' : 'asc'
      );
    } else {
      this.sortField.set(field);
      this.sortDirection.set('asc');
    }

    this.page.set(0); // reset pagination
  }

  trackById(_: number, deal: Deal) {
    return deal.id;
  }


  // ---------------- Pagination ----------------
  nextPage(): void {
    if (this.page() < this.totalPages() - 1) {
      this.page.set(this.page() + 1);
    }
  }

  prevPage(): void {
    if (this.page() > 0) {
      this.page.set(this.page() - 1);
    }
  }

}
