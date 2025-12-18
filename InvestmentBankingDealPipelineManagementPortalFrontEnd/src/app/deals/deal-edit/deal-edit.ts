import { Component, signal } from '@angular/core';
import { Validators, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../auth/services/auth.service';
import { Deal, DealsService } from '../services/deal.sevice';
import { CommonModule } from '@angular/common';

interface StageUpdateResponse {
  message: string;
  deal: any; // replace `any` with your Deal type if you have it
}

interface DealValueUpdateResponse {
  message: string;
}


@Component({
  selector: 'app-deal-edit',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './deal-edit.html',
  styleUrl: './deal-edit.css',
})
export class DealEdit {

  id!: string;
  role;
  dealForm;
  stageControl;
  dealValueControl;
  noteForm: string | any;
 

  loading = signal(false);
  error = signal<string | null>(null);

  deal: Deal | undefined;
  notes: any[] = [];

  success = signal<string | null>(null);


  

  stages = [
    'PROSPECT',
    'UNDER_EVALUATION',
    'TERM_SHEET_SUBMITTED',
    'CLOSED',
    'LOST'
  ];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private dealsService: DealsService,
    private router: Router,
    private auth: AuthService
  ) {
     this.role = this.auth.getRole();

     // BASIC UPDATE (NO NOTES)
  this.dealForm = this.fb.nonNullable.group({
    clientName: ['', Validators.required],
    dealType: ['', Validators.required],
    sector: ['', Validators.required],
    summary: ['']
  });

  // STAGE
  this.stageControl = this.fb.control('', Validators.required);

  // ADMIN
  this.dealValueControl = this.fb.control<number | null>(null);

  // ADD NOTE (SEPARATE)
  this.noteForm = this.fb.nonNullable.group({
    note: ['', Validators.required]
  });

  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.loadDeal();
  }

  goBack() {
    this.router.navigate(['/deals']);
  }


  loadDeal() {
    this.loading.set(true);

    this.dealsService.getDealById(this.id).subscribe({
      next: deal => {
        this.deal = deal;
        this.notes = deal.notes ?? [];

        console.log(deal);

        this.dealForm.patchValue(deal);
        this.stageControl.setValue(deal.currentStage);

        if (this.role === 'ADMIN') {
          this.dealValueControl.setValue(deal.dealValue);
        }

        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load deal');
        this.loading.set(false);
      }
    });
  }

  // PUT
  saveBasic() {
    if (this.dealForm.invalid) return;

    this.dealsService.updateDeal(this.id, this.dealForm.getRawValue())
      .subscribe();
  }

  isOwner(): boolean {
    return this.deal?.createdBy === this.auth.getUsername();
  }


  // PATCH stage
  updateStage() {
    this.dealsService.updateStage(this.id, this.stageControl.value!)
      .subscribe({
        next: (res: StageUpdateResponse) => {
          this.success.set(res.message);
          this.stageControl.setValue(res.deal.currentStage);

          // Clear success after 3 seconds (optional)
          setTimeout(() => this.success.set(null), 3000);
        },
        error: (err) => {
          this.error.set('Failed to update stage');
          setTimeout(() => this.error.set(null), 3000);
          console.error(err); // optional: log full error
        }
      });
  }



  // ADMIN value
  updateDealValue() {
    if (this.role !== 'ADMIN') return;

    this.dealsService.updateDealValue(this.id, this.dealValueControl.value!)
      .subscribe({
        next: (res: DealValueUpdateResponse) => {
          this.success.set(res.message);
          setTimeout(() => this.success.set(null), 3000);
        },
        error: (err) => {
          this.error.set('Failed to update deal value');
          setTimeout(() => this.error.set(null), 3000);
          console.error(err);
        }
      });
  }



  // ➕ ADD NOTE
  addNote() {
    if (this.noteForm.invalid) return;

    this.dealsService.addNote(
      this.id,
      this.noteForm.value.note
    ).subscribe(updatedDeal => {
      this.notes = updatedDeal.notes;
      this.noteForm.reset();
    });
  }

  deleteDeal() {
    if (!confirm('Delete this deal?')) return;

    this.dealsService.deleteDeal(this.id).subscribe(() => {
      this.router.navigate(['/deals']);
    });
  }
}
