import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

export interface Deal {
  id: string;
  clientName: string;
  dealType: string;
  sector: string;
  stage: string;
  dealValue?: number; 
  createdBy: string;
  assignedTo: string;
}

export interface DealPage {
  content: Deal[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  page:number;

}


interface StageUpdateResponse {
  message: string;
  deal: any; // replace `any` with your Deal type if you have it
}

interface DealValueUpdateResponse {
  message: string;
}


@Injectable({ providedIn: 'root' })
export class DealsService {

  private readonly API = '/api/deals';

  constructor(private http: HttpClient) {}

  getDeals(page: number,size: number,sortBy: string,direction: 'asc' | 'desc') {
    return this.http.get<DealPage>(
      this.API,{params: {page,size,sortBy,direction}});
    }

  createDeal(payload: any) {
  return this.http.post(this.API, payload);
  }

  getDealById(id: string) {
    return this.http.get<any>(`${this.API}/${id}`);
  }

  updateDeal(id: string, payload: any) {
    return this.http.put(`${this.API}/${id}`, payload);
  }

  updateStage(id: string, stage: string) {
    return this.http.patch<StageUpdateResponse>(`${this.API}/${id}/stage`, { stage });
  }

  updateDealValue(id: string, dealValue: number) {
    return this.http.patch<DealValueUpdateResponse>(`${this.API}/${id}/value`, { dealValue });
  }

  deleteDeal(id: string) {
    return this.http.delete(`${this.API}/${id}`);
  }

  addNote(id: string, note: string) {
    return this.http.post<any>(
      `${this.API}/${id}/notes`,
      { note }
    );
  }




}

