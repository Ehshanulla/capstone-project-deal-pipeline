import { Component, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DealsService, Deal } from '../services/deal.sevice';
import { AuthService } from '../../auth/services/auth.service';
import { DragDropModule, CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

type Stage =
  | 'PROSPECT'
  | 'UNDER_EVALUATION'
  | 'TERM_SHEET_SUBMITTED'
  | 'CLOSED'
  | 'LOST'
  | any;

@Component({
  selector: 'app-kanban',
  imports: [CommonModule, RouterModule,  DragDropModule],
  templateUrl: './kanban.html',
  styleUrl: './kanban.css',
})
export class Kanban {
  // ---------------- Signals ----------------
  deals = signal<Deal[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  role: string | null;

  stages: Stage[] = [
    'PROSPECT',
    'UNDER_EVALUATION',
    'TERM_SHEET_SUBMITTED',
    'CLOSED',
    'LOST'
  ];

  constructor(
    private dealsService: DealsService,
    private auth: AuthService
  ) {
    this.role = this.auth.getRole();

    this.loadDeals();
  }

  // ---------------- Load Deals ----------------
  loadDeals() {
    this.loading.set(true);

    this.dealsService.getDeals(0,100,'createdAt','desc').subscribe({
      next: deals => {
        this.deals.set(deals.content);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load deals');
        this.loading.set(false);
      }
    });
  }

  // ---------------- Kanban Computed ----------------
  kanban = computed(() => {
    const map: Record<Stage, Deal[]> = {
      PROSPECT: [],
      UNDER_EVALUATION: [],
      TERM_SHEET_SUBMITTED: [],
      CLOSED: [],
      LOST: []
    };


    for (const deal of this.deals()) {
      map[deal.stage as Stage]?.push(deal);
    }

    return map;
  });

  // ---------------- Drag & Drop ----------------
  onDrop(event: CdkDragDrop<Deal[]>, targetStage: Stage) {
  if (event.previousContainer.id === event.container.id) {
    moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    this.deals.set([...this.deals()]); // trigger recompute
    return;
  }

  const deal = event.previousContainer.data[event.previousIndex];

  // Optimistic UI
  deal.stage = targetStage;
  transferArrayItem(
    event.previousContainer.data,
    event.container.data,
    event.previousIndex,
    event.currentIndex
  );
  this.deals.set([...this.deals()]);

  this.dealsService.updateStage(deal.id, targetStage).subscribe({
    next: () => {
      // success: already updated in UI
    },
    error: () => {
      // rollback
      deal.stage = event.previousContainer.id as Stage;
      transferArrayItem(
        event.container.data,
        event.previousContainer.data,
        event.currentIndex,
        event.previousIndex
      );
      this.deals.set([...this.deals()]);
    }
  });
}




  connectedLists() {
    return this.stages.map(stage => stage); // returns ["PROSPECT", "UNDER_EVALUATION", ...]
  }

}
