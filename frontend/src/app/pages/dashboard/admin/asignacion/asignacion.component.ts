import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VueloService, VueloResponse } from '../../../../core/services/vuelo.service';
import { TripulanteService, TripulanteResponse } from '../../../../core/services/tripulante.service';
import Swal from 'sweetalert2';

interface TripulanteSeleccionable extends TripulanteResponse {
  seleccionado: boolean;
}

@Component({
  selector: 'app-asignacion',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './asignacion.component.html',
  styleUrls: ['./asignacion.component.scss']
})
export class AsignacionComponent implements OnInit {

  vuelos: VueloResponse[] = [];
  tripulantes: TripulanteSeleccionable[] = [];
  vueloSeleccionado: VueloResponse | null = null;
  loading = true;
  loadingTripulantes = true;

  constructor(
    private vueloService: VueloService,
    private tripulanteService: TripulanteService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarVuelos();
    this.cargarTripulantes();
  }

  cargarVuelos(): void {
    this.loading = true;
    this.vueloService.listar().subscribe({
      next: (data) => {
        this.vuelos = data.filter(v => v.estado === 'PROGRAMADO' || v.estado === 'RETRASADO');
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        Swal.fire('Error', 'No se pudieron cargar los vuelos.', 'error');
      }
    });
  }

  cargarTripulantes(): void {
    this.loadingTripulantes = true;
    this.tripulanteService.listar().subscribe({
      next: (data) => {
        this.tripulantes = data
          .filter(t => t.activo && t.estado === 'DISPONIBLE')
          .map(t => ({ ...t, seleccionado: false }));
        this.loadingTripulantes = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadingTripulantes = false;
        Swal.fire('Error', 'No se pudieron cargar los tripulantes.', 'error');
      }
    });
  }

  seleccionarVuelo(vuelo: VueloResponse): void {
    this.vueloSeleccionado = vuelo;
    this.tripulantes = this.tripulantes.map(t => ({
      ...t,
      seleccionado: vuelo.tripulacionIds?.includes(t.id) ?? false
    }));
    this.cdr.detectChanges();
  }

  initials(nombre: string): string {
    return nombre.split(' ').slice(0, 2).map(n => n[0]).join('').toUpperCase();
  }

  formatFecha(fecha: string): string {
    return fecha ? fecha.slice(0, 16).replace('T', ' ') : '';
  }

  get tripulantesSeleccionados(): number {
    return this.tripulantes.filter(t => t.seleccionado).length;
  }

  confirmar(): void {
    if (!this.vueloSeleccionado) {
      Swal.fire('Vuelo requerido', 'Seleccione un vuelo antes de confirmar.', 'warning');
      return;
    }

    const ids = this.tripulantes.filter(t => t.seleccionado).map(t => t.id);

    Swal.fire({
      title: 'Confirmar asignacion?',
      text: `Se asignaran ${ids.length} tripulante(s) al vuelo ${this.vueloSeleccionado.numVuelo}.`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Si, confirmar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        const dto = {
          numVuelo:         this.vueloSeleccionado!.numVuelo,
          origenId:         this.vueloSeleccionado!.origenId,
          destinoId:        this.vueloSeleccionado!.destinoId,
          fechaHoraSalida:  this.vueloSeleccionado!.fechaHoraSalida.slice(0, 19),
          fechaHoraLlegada: this.vueloSeleccionado!.fechaHoraLlegada
            ? this.vueloSeleccionado!.fechaHoraLlegada.slice(0, 19)
            : undefined,
          avionId:          this.vueloSeleccionado!.avionId,
          pilotoId:         this.vueloSeleccionado!.pilotoId,
          estado:           this.vueloSeleccionado!.estado,
          tripulacionIds:   ids
        };

        this.vueloService.actualizar(this.vueloSeleccionado!.id, dto).subscribe({
          next: () => {
            Swal.fire('Asignado!', 'La tripulacion fue asignada correctamente.', 'success');
            this.cargarVuelos();
            this.cargarTripulantes();
            this.vueloSeleccionado = null;
          },
          error: (err) => {
            const msg = typeof err.error === 'string' ? err.error : 'Error al asignar tripulacion.';
            Swal.fire('Error', msg, 'error');
          }
        });
      }
    });
  }
}