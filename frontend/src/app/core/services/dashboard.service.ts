import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DashboardService {

  private base = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  getStats(): Observable<any> {
    return forkJoin({
      aviones: this.http.get<any[]>(`${this.base}/aviones`),
      pilotos: this.http.get<any[]>(`${this.base}/pilotos`),
      tripulacion: this.http.get<any[]>(`${this.base}/tripulantes`),
      vuelos: this.http.get<any[]>(`${this.base}/vuelos`),
    });
  }
}