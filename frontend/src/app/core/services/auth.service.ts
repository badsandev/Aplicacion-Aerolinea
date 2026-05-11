import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = 'http://localhost:8081/api/auth';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  registro(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/registro`, data);
  }

  guardarSesion(token: string, rol: string, username: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('rol', rol);
    localStorage.setItem('username', username);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRol(): string | null {
    return localStorage.getItem('rol');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('rol');
    localStorage.removeItem('username');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

recuperarPassword(email: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/recuperar-password`, { email }, 
    { responseType: 'text' }); // ← agregar
}

cambiarPassword(token: string, password: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/cambiar-password`, { token, password },
    { responseType: 'text' }); // ← agregar
}


}