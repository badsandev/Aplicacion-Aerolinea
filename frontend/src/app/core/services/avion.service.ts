import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AvionResponse {
  id:           number;
  codigo:       string;
  tipo:         string;
  base:         string;
  baseId:       number;
  capacidad:    number;
  horasDeVuelo: number;
  estado:       string;
}

export interface AvionDTO {
  codigo:               string;
  tipo:                 string;
  capacidad:            number;
  horasDeVuelo?:        number;
  estado?:              string;
  yearFabricacion?:     number;
  ultimoMantenimiento?: string;
  baseId?:              number;
}

export interface BaseResponse {
  id:     number;
  nombre: string;
}

@Injectable({ providedIn: 'root' })
export class AvionService {

  private apiUrl = 'http://localhost:8081/api/aviones';
  private basesUrl = 'http://localhost:8081/api/bases';

  constructor(private http: HttpClient) {}

  listar(): Observable<AvionResponse[]> {
    return this.http.get<AvionResponse[]>(this.apiUrl);
  }

  crear(dto: AvionDTO): Observable<AvionResponse> {
    return this.http.post<AvionResponse>(this.apiUrl, dto);
  }

  actualizar(id: number, dto: AvionDTO): Observable<AvionResponse> {
    return this.http.put<AvionResponse>(`${this.apiUrl}/${id}`, dto);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  mantenimiento(id: number): Observable<AvionResponse> {
    return this.http.put<AvionResponse>(`${this.apiUrl}/${id}/mantenimiento`, {});
  }

  reactivar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/reactivar`, {}, { responseType: 'text' });
  }

  listarBases(): Observable<BaseResponse[]> {
    return this.http.get<BaseResponse[]>(this.basesUrl);
  }
}