import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UsuarioResponse {
  id: number;
  username: string;
  email: string;
  rol: string;
  nombre: string | null;
  codigo: string | null;
  base: string | null;
  activo: boolean;
}

export interface UsuarioDTO {
  username: string;
  email: string;
  password?: string;
  rol: string;
  nombre?: string;
  codigo?: string;
  baseId?: number;
  licencia?: string;
  horasDeVuelo?: number;
  rolTripulante?: string;
}

@Injectable({ providedIn: 'root' })
export class UsuarioService {

  private apiUrl = 'http://localhost:8081/api/usuarios';

  constructor(private http: HttpClient) {}

  listar(): Observable<UsuarioResponse[]> {
    return this.http.get<UsuarioResponse[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.apiUrl}/${id}`);
  }

  crear(dto: UsuarioDTO): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(this.apiUrl, dto);
  }

  actualizar(id: number, dto: UsuarioDTO): Observable<UsuarioResponse> {
    return this.http.put<UsuarioResponse>(`${this.apiUrl}/${id}`, dto);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  reactivar(id: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/reactivar`, {}, { responseType: 'text' });
  }
}