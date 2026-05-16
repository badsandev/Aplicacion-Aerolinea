import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BaseResponse {
  id:                  number;
  nombre:              string;
  codigoIata:          string;
  codigoIcao:          string;
  ciudad:              string;
  pais:                string;
  esBaseMantenimiento: boolean;
}

export interface BaseDTO {
  nombre:              string;
  codigoIata:          string;
  codigoIcao:          string;
  ciudad?:             string;
  pais?:               string;
  esBaseMantenimiento: boolean;
}

@Injectable({ providedIn: 'root' })
export class BaseService {

  private apiUrl = 'http://localhost:8081/api/bases';

  constructor(private http: HttpClient) {}

  listar(): Observable<BaseResponse[]> {
    return this.http.get<BaseResponse[]>(this.apiUrl);
  }

  crear(dto: BaseDTO): Observable<BaseResponse> {
    return this.http.post<BaseResponse>(this.apiUrl, dto);
  }

  actualizar(id: number, dto: BaseDTO): Observable<BaseResponse> {
    return this.http.put<BaseResponse>(`${this.apiUrl}/${id}`, dto);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  reactivar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/reactivar`, {}, { responseType: 'text' });
  }
}