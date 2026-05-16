import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BaseResponse } from './avion.service';

export enum EstadoPersonalAereo {
  DISPONIBLE = 'DISPONIBLE',
  EN_VUELO = 'EN_VUELO',
  DESCANSO = 'DESCANSO',
  INACTIVO = 'INACTIVO',
  SIN_ESTADO = 'SIN_ESTADO'
}

export interface PilotoResponse {
  id: number;
  codigo: string;
  nombre: string;
  licencia: string;
  horasDeVuelo: number;
  base: string;
  baseId?: number;
  estado: EstadoPersonalAereo;
  activo: boolean;
  username?: string;
  email?: string;
}

export interface PilotoDTO {
  codigo: string;
  nombre: string;
  licencia: string;
  horasDeVuelo: number;
  baseId?: number;
  estado?: EstadoPersonalAereo;
}

@Injectable({ providedIn: 'root' })
export class PilotoService {

  private apiUrl = 'http://localhost:8081/api/pilotos';
  private basesUrl = 'http://localhost:8081/api/bases';

  constructor(private http: HttpClient) {}

  listar(): Observable<PilotoResponse[]> {
    return this.http.get<PilotoResponse[]>(this.apiUrl);
  }

  crear(dto: any): Observable<PilotoResponse> {
    return this.http.post<PilotoResponse>(this.apiUrl, dto);
  }

  actualizar(id: number, dto: any): Observable<PilotoResponse> {
    return this.http.put<PilotoResponse>(`${this.apiUrl}/${id}`, dto);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  reactivar(id: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/reactivar`, {}, { responseType: 'text' });
  }

  listarBases(): Observable<BaseResponse[]> {
    return this.http.get<BaseResponse[]>(this.basesUrl);
  }
}