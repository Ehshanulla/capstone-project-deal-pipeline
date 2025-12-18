// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  role: 'ADMIN' | 'USER';
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = '/api/auth/login';

  private loggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
  loggedIn$ = this.loggedInSubject.asObservable();

  private roleSubject = new BehaviorSubject<string | null>(
    localStorage.getItem('role'));
  role$ = this.roleSubject.asObservable();

  constructor(private http: HttpClient) {}

   login(request: any): Observable<any> {
    return this.http.post<any>(this.API_URL, request).pipe(
      tap(res => {
        localStorage.setItem('jwt', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('username', res.username);

        // 🔥 Notify Angular
        this.loggedInSubject.next(true);
        this.roleSubject.next(res.role);
      })
    );
  }

  redirectAfterLogin(): string {
    const role = this.getRole();
    return role === 'ADMIN' ? '/admin' : '/deals';
  }


   logout(): void {
    localStorage.clear();

    // 🔥 Notify Angular
    this.loggedInSubject.next(false);
    this.roleSubject.next(null);
  }


  isLoggedIn(): boolean {
    return this.hasToken();
  }

   getRole(): string | null {
    return this.roleSubject.value;
  }

  getUsername(): string | null{
    return localStorage.getItem('username');
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  // 🔑 Helper method
  private hasToken(): boolean {
    return !!localStorage.getItem('jwt');
  }
}
