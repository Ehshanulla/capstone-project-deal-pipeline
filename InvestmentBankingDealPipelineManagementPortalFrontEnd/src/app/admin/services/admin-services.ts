import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: string;
  username: string;
  email: string;
  role: 'ADMIN' | 'USER';
  active: boolean;
}

export interface CreateUserRequest {
  username: string;
  email: string;
  password: string;
  role: 'ADMIN' | 'USER';
}

export interface UpdateUserStatusRequest {
  active: boolean;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class AdminServices {
  private API = '/api/admin/users';

  constructor(private http: HttpClient) {}

   getUsers(page = 0,size = 10): Observable<PageResponse<User>> {
    return this.http.get<PageResponse<User>>(this.API, {
      params: {page,size}
    });
  }
  createUser(data: CreateUserRequest| any): Observable<User> {
    return this.http.post<User>(this.API, data);
  }

  updateUserStatus(id: string , active: boolean | any): Observable<User> {
    return this.http.put<User>(`${this.API}/${id}/status`, { active });
  }
}
