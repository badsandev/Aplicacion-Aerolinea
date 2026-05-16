import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface VueloResponse {
  id: number;
  numVuelo: string;
  origen: string;
  origenId: number;
  destino: string;
  destinoId: number;
  fechaHoraSalida: string;
  fechaHoraLlegada: string;
  estado: string;
  avion: string;
  avionId: number;
  piloto: string;
  pilotoId: number;
  tripulacion: string[];
  tripulacionIds: number[];
}

export interface VueloDTO {
  numVuelo:         string;
  origenId:         number;
  destinoId:        number;
  fechaHoraSalida:  string;
  fechaHoraLlegada?: string;
  estado?:          string;
  avionId:          number;
  pilotoId:         number;
  tripulacionIds?:  number[];
}

@Injectable({ providedIn: 'root' })
export class VueloService {

  private apiUrl = 'http://localhost:8081/api/vuelos';

  constructor(private http: HttpClient) {}

  listar(): Observable<VueloResponse[]> {
    return this.http.get<VueloResponse[]>(this.apiUrl);
  }

  crear(dto: VueloDTO): Observable<VueloResponse> {
    return this.http.post<VueloResponse>(this.apiUrl, dto);
  }

  actualizar(id: number, dto: VueloDTO): Observable<VueloResponse> {
    return this.http.put<VueloResponse>(`${this.apiUrl}/${id}`, dto);
  }

  cancelar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/cancelar`, {}, { responseType: 'text' });
  }

  retrasar(id: number, nuevaFecha: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/retrasar`, { nuevaFechaSalida: nuevaFecha });
  }

  aterrizar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/aterrizar`, {}, { responseType: 'text' });
  }
}