import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { VueloService, VueloResponse, VueloDTO } from '../../../../core/services/vuelo.service';
import { AvionService, AvionResponse } from '../../../../core/services/avion.service';
import { PilotoService, PilotoResponse } from '../../../../core/services/piloto.service';
import { BaseService, BaseResponse } from '../../../../core/services/base.service';
import { TripulanteService, TripulanteResponse } from '../../../../core/services/tripulante.service';
import Swal from 'sweetalert2';

interface VueloForm {
  numVuelo:         string;
  origenId?:        number;
  destinoId?:       number;
  fechaHoraSalida?: string;
  fechaHoraLlegada?: string;
  avionId?:         number;
  pilotoId?:        number;
  tripulacionIds:   number[];
  estado?:          string;
}

@Component({
  selector: 'app-vuelos',
  standalone: true,
  imports: [FormsModule, CommonModule, DatePipe],  
  templateUrl: './vuelos.component.html',
  styleUrl: './vuelos.component.scss'
})
export class VuelosComponent implements OnInit {

  vuelos:      VueloResponse[]     = [];
  aviones:     AvionResponse[]     = [];
  pilotos:     PilotoResponse[]    = [];
  bases:       BaseResponse[]      = [];
  tripulantes: TripulanteResponse[] = [];

  loading   = true;
  showForm  = false;
  editando  = false;
  editId:   number | null = null;

  estados = ['PROGRAMADO', 'EN_VUELO', 'ATERRIZADO', 'RETRASADO', 'CANCELADO'];

  nuevo: Partial<VueloForm> = this.emptyForm();

  constructor(
    private vueloService:      VueloService,
    private avionService:      AvionService,
    private pilotoService:     PilotoService,
    private baseService:       BaseService,
    private tripulanteService: TripulanteService,
    private cdr:               ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.cargarSelects();
  }

  private emptyForm(): Partial<VueloForm> {
    return { numVuelo: '', tripulacionIds: [] };
  }

  get total()       { return this.vuelos.length; }
  get programados() { return this.vuelos.filter(v => v.estado === 'PROGRAMADO').length; }
  get enVuelo()     { return this.vuelos.filter(v => v.estado === 'EN_VUELO').length; }

  cargar(): void {
    this.loading = true;
    this.cdr.detectChanges();

    this.vueloService.listar().subscribe({
      next: (data) => {
        this.vuelos  = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
        Swal.fire('Error', 'No se pudieron cargar los vuelos.', 'error');
      }
    });
  }

  cargarSelects(): void {
    this.avionService.listar().subscribe({ next: d => { this.aviones = d; this.cdr.detectChanges(); } });
    this.pilotoService.listar().subscribe({ next: d => { this.pilotos = d.filter(p => p.activo); this.cdr.detectChanges(); } });
    this.baseService.listar().subscribe({ next: d => { this.bases = d; this.cdr.detectChanges(); } });
    this.tripulanteService.listar().subscribe({ next: d => { this.tripulantes = d.filter(t => t.activo); this.cdr.detectChanges(); } });
  }

  openForm(): void {
    this.editando = false;
    this.editId   = null;
    this.nuevo    = this.emptyForm();
    this.showForm = true;
    this.cdr.detectChanges();
  }

  closeForm(): void {
    this.showForm = false;
    this.editando = false;
    this.editId   = null;
    this.nuevo    = this.emptyForm();
  }

  editar(v: VueloResponse): void {
  this.showForm = false;
  this.cdr.detectChanges();
  setTimeout(() => {
    this.editando = true;
    this.editId = v.id;
    this.nuevo = {
      numVuelo: v.numVuelo,
      origenId: v.origenId,
      destinoId: v.destinoId,
      fechaHoraSalida: v.fechaHoraSalida
        ? v.fechaHoraSalida.slice(0, 16)
        : '',
      fechaHoraLlegada: v.fechaHoraLlegada
        ? v.fechaHoraLlegada.slice(0, 16)
        : '',
      avionId: v.avionId,
      pilotoId: v.pilotoId,
      estado: v.estado,
      tripulacionIds: [...(v.tripulacionIds || [])]
    };
    this.showForm = true;
    this.cdr.detectChanges();

  }, 0);
}

  toggleTripulante(id: number): void {
    const ids = this.nuevo.tripulacionIds ?? [];
    const idx = ids.indexOf(id);
    if (idx === -1) ids.push(id);
    else ids.splice(idx, 1);
    this.nuevo.tripulacionIds = [...ids];
  }

  isTripulanteSelected(id: number): boolean {
    return (this.nuevo.tripulacionIds ?? []).includes(id);
  }

  private validar(): boolean {
    if (!this.nuevo.numVuelo?.trim()) {
      Swal.fire('Campo requerido', 'El número de vuelo es obligatorio.', 'warning');
      return false;
    }
    if (!this.nuevo.origenId) {
      Swal.fire('Campo requerido', 'Seleccione una base de origen.', 'warning');
      return false;
    }
    if (!this.nuevo.destinoId) {
      Swal.fire('Campo requerido', 'Seleccione una base de destino.', 'warning');
      return false;
    }
    if (this.nuevo.origenId === this.nuevo.destinoId) {
      Swal.fire('Error', 'El origen y destino no pueden ser iguales.', 'error');
      return false;
    }
    if (!this.nuevo.fechaHoraSalida) {
      Swal.fire('Campo requerido', 'La fecha y hora de salida son obligatorias.', 'warning');
      return false;
    }
    if (!this.nuevo.avionId) {
      Swal.fire('Campo requerido', 'Seleccione un avión.', 'warning');
      return false;
    }
    if (!this.nuevo.pilotoId) {
      Swal.fire('Campo requerido', 'Seleccione un piloto.', 'warning');
      return false;
    }
    return true;
  }

 guardar(): void {
    if (!this.validar()) return;

    const formatFecha = (f: string | undefined) => 
        f ? (f.length === 16 ? f + ':00' : f) : undefined;

    const dto: VueloDTO = {
        numVuelo:         this.nuevo.numVuelo!,
        origenId:         Number(this.nuevo.origenId),
        destinoId:        Number(this.nuevo.destinoId),
        fechaHoraSalida:  formatFecha(this.nuevo.fechaHoraSalida)!,
        fechaHoraLlegada: formatFecha(this.nuevo.fechaHoraLlegada),
        avionId:          Number(this.nuevo.avionId),
        pilotoId:         Number(this.nuevo.pilotoId),
        tripulacionIds:   this.nuevo.tripulacionIds,
        estado:           this.nuevo.estado || undefined
    };

    console.log('DTO enviado:', JSON.stringify(dto));

    const request$ = this.editando && this.editId
        ? this.vueloService.actualizar(this.editId, dto)
        : this.vueloService.crear(dto);

    request$.subscribe({
        next: () => {
            Swal.fire(
                this.editando ? '¡Actualizado!' : '¡Registrado!',
                this.editando ? 'Vuelo actualizado correctamente.' : 'Vuelo creado exitosamente.',
                'success'
            );
            this.closeForm();
            this.cargar();
        },
        error: (err) => {
            const msg = typeof err.error === 'string' ? err.error : err.error?.message || 'Error al guardar el vuelo.';
            Swal.fire('Error', msg, 'error');
        }
    });
}

  cancelarVuelo(id: number): void {
    Swal.fire({
      title: '¿Cancelar vuelo?',
      text: 'El vuelo será marcado como cancelado.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, cancelar',
      cancelButtonText: 'No'
    }).then(r => {
      if (r.isConfirmed) {
        this.vueloService.cancelar(id).subscribe({
          next: () => { Swal.fire('Cancelado', 'El vuelo fue cancelado.', 'success'); this.cargar(); },
          error: () => Swal.fire('Error', 'No se pudo cancelar el vuelo.', 'error')
        });
      }
    });
  }

  aterrizar(id: number): void {
    Swal.fire({
      title: '¿Registrar aterrizaje?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, aterrizar',
      cancelButtonText: 'Cancelar'
    }).then(r => {
      if (r.isConfirmed) {
        this.vueloService.aterrizar(id).subscribe({
          next: () => { Swal.fire('¡Aterrizado!', 'Vuelo completado.', 'success'); this.cargar(); },
          error: () => Swal.fire('Error', 'No se pudo registrar el aterrizaje.', 'error')
        });
      }
    });
  }

  estadoClass(estado: string): string {
    switch (estado) {
      case 'PROGRAMADO': return 'badge--blue';
      case 'EN_VUELO':   return 'badge--green';
      case 'ATERRIZADO': return 'badge--gray';
      case 'RETRASADO':  return 'badge--yellow';
      case 'CANCELADO':  return 'badge--red';
      default:           return 'badge--gray';
    }
  }
}