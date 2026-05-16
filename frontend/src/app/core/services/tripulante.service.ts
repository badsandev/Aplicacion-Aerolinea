import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BaseResponse } from './avion.service';

export enum RolTripulante {
  COPILOTO = 'COPILOTO',
  AZAFATA = 'AZAFATA',
  SOBRECARGO = 'SOBRECARGO',
  TECNICO = 'TECNICO',
  SEGURIDAD = 'SEGURIDAD',
  MEDICO = 'MEDICO'
}

export enum EstadoTripulante {
  DISPONIBLE = 'DISPONIBLE',
  EN_VUELO = 'EN_VUELO',
  DESCANSO = 'DESCANSO',
  INACTIVO = 'INACTIVO'
}

export interface TripulanteResponse {
  id: number;
  codigo: string;
  nombre: string;
  rol: RolTripulante;
  estado: EstadoTripulante;
  base: string;
  baseId?: number;
  activo: boolean;
}

export interface TripulanteDTO {
  username?: string;
  email?: string;
  password?: string;

  codigo: string;
  nombre: string;
  rol?: RolTripulante;
  estado?: EstadoTripulante;
  baseId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class TripulanteService {
  private apiUrl = 'http://localhost:8081/api/tripulantes';
  private basesUrl = 'http://localhost:8081/api/bases';

  constructor(private http: HttpClient) {}

  listar(): Observable<TripulanteResponse[]> {
    return this.http.get<TripulanteResponse[]>(this.apiUrl);
  }

  crear(dto: TripulanteDTO): Observable<TripulanteResponse> {
    return this.http.post<TripulanteResponse>(this.apiUrl, dto);
  }

  actualizar(id: number, dto: TripulanteDTO): Observable<TripulanteResponse> {
    return this.http.put<TripulanteResponse>(`${this.apiUrl}/${id}`, dto);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  reactivar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/reactivar`, {}, { responseType: 'text' });
  }

  listarBases(): Observable<BaseResponse[]> {
    return this.http.get<BaseResponse[]>(this.basesUrl);
  }
}