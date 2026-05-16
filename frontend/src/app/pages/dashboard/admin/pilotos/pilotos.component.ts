import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, DecimalPipe } from '@angular/common';

import {
  PilotoService,
  PilotoResponse,
  EstadoPersonalAereo
} from '../../../../core/services/piloto.service';

import { UsuarioService } from '../../../../core/services/usuario.service';
import { BaseResponse } from '../../../../core/services/avion.service';
import Swal from 'sweetalert2';

interface PilotoForm {
  username?: string;
  email?: string;
  password?: string;
  codigo?: string;
  nombre?: string;
  licencia?: string;
  horasDeVuelo?: number;
  baseId?: number;
  estado?: EstadoPersonalAereo;
}

@Component({
  selector: 'app-pilotos',
  standalone: true,
  imports: [FormsModule, DecimalPipe, CommonModule],
  templateUrl: './pilotos.component.html',
  styleUrl: './pilotos.component.scss'
})
export class PilotosComponent implements OnInit {

  pilotos: PilotoResponse[] = [];
  bases: BaseResponse[] = [];
  loading = true;
  showForm = false;
  editando = false;
  editId: number | null = null;

  licencias = [
    'ATPL (Airline Transport Pilot)',
    'CPL (Commercial Pilot)',
    'PPL (Private Pilot)'
  ];

  estados = Object.values(EstadoPersonalAereo).filter(e => e !== EstadoPersonalAereo.SIN_ESTADO);

  nuevo: Partial<PilotoForm> = this.emptyForm();

  constructor(
    private pilotoService: PilotoService,
    private usuarioService: UsuarioService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarBases();
    this.cargar();
  }

  private emptyForm(): Partial<PilotoForm> {
    return {
      username: '',
      email: '',
      password: '',
      codigo: '',
      nombre: '',
      licencia: '',
      horasDeVuelo: undefined,
      baseId: undefined,
      estado: EstadoPersonalAereo.DISPONIBLE
    };
  }

  get total(): number { return this.pilotos.length; }
  get activos(): number { return this.pilotos.filter(p => p.activo).length; }

  private validarCampos(): boolean {
    const soloLetras = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!this.editando) {
      if (!this.nuevo.username?.trim()) {
        Swal.fire('Campo requerido', 'El nombre de usuario es obligatorio.', 'warning');
        return false;
      }
      if (!this.nuevo.email?.trim() || !emailRegex.test(this.nuevo.email)) {
        Swal.fire('Email inválido', 'Ingrese un correo electrónico válido.', 'warning');
        return false;
      }
      if (!this.nuevo.password || this.nuevo.password.length < 6) {
        Swal.fire('Contraseña inválida', 'La contraseña debe tener mínimo 6 caracteres.', 'warning');
        return false;
      }
    }

    if (!this.nuevo.codigo?.trim()) {
      Swal.fire('Campo requerido', 'El código del piloto es obligatorio.', 'warning');
      return false;
    }
    if (!this.nuevo.nombre?.trim()) {
      Swal.fire('Campo requerido', 'El nombre completo es obligatorio.', 'warning');
      return false;
    }
    if (!soloLetras.test(this.nuevo.nombre.trim())) {
      Swal.fire('Nombre inválido', 'El nombre solo debe contener letras y espacios.', 'error');
      return false;
    }
    if (!this.nuevo.licencia) {
      Swal.fire('Campo requerido', 'Debe seleccionar una licencia.', 'warning');
      return false;
    }
    if (this.nuevo.horasDeVuelo === undefined || this.nuevo.horasDeVuelo === null) {
      Swal.fire('Campo requerido', 'Las horas de vuelo son obligatorias.', 'warning');
      return false;
    }
    if (this.nuevo.horasDeVuelo < 0) {
      Swal.fire('Valor inválido', 'Las horas de vuelo no pueden ser negativas.', 'error');
      return false;
    }
    if (!this.nuevo.estado) {
      Swal.fire('Campo requerido', 'Debe seleccionar un estado personal.', 'warning');
      return false;
    }

    return true;
  }

  guardar(): void {
    if (!this.validarCampos()) return;

    const dtoBase = {
      codigo:       this.nuevo.codigo!,
      nombre:       this.nuevo.nombre!,
      licencia:     this.nuevo.licencia!,
      horasDeVuelo: Number(this.nuevo.horasDeVuelo),
      baseId:       this.nuevo.baseId != null ? Number(this.nuevo.baseId) : undefined,
      estado:       this.nuevo.estado
    };

    const dto: any = this.editando
      ? dtoBase
      : { ...dtoBase, username: this.nuevo.username, email: this.nuevo.email, password: this.nuevo.password, rol: 'PILOTO' };

    const request$ = this.editando && this.editId
      ? this.usuarioService.actualizar(this.editId, dto)
      : this.usuarioService.crear(dto);

    request$.subscribe({
      next: () => {
        Swal.fire(
          this.editando ? '¡Actualizado!' : '¡Registrado!',
          this.editando ? 'Piloto actualizado correctamente.' : 'El piloto ha sido creado exitosamente.',
          'success'
        );
        this.closeForm();
        this.cargar();
      },
      error: (err) => {
        const msg = typeof err.error === 'string' ? err.error : err.error?.message || 'Error al guardar el piloto.';
        Swal.fire('Error', msg, 'error');
      }
    });
  }

  eliminar(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'El piloto será marcado como inactivo.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, desactivar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pilotoService.eliminar(id).subscribe({
          next: () => {
            Swal.fire('Desactivado', 'El piloto ha sido marcado como inactivo.', 'success');
            this.cargar();
          },
          error: () => Swal.fire('Error', 'No se pudo desactivar el piloto.', 'error')
        });
      }
    });
  }

  reactivar(id: number): void {
    Swal.fire({
      title: '¿Reactivar piloto?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, reactivar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pilotoService.reactivar(id).subscribe({
          next: () => {
            Swal.fire('Reactivado', 'El piloto está operativo nuevamente.', 'success');
            this.cargar();
          },
          error: () => Swal.fire('Error', 'No se pudo reactivar el piloto.', 'error')
        });
      }
    });
  }

  cargar(): void {
    this.loading = true;
    this.cdr.detectChanges();

    this.pilotoService.listar().subscribe({
      next: (data) => {
        this.pilotos = data ?? [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR PILOTOS:', err);
        this.pilotos = [];
        this.loading = false;
        this.cdr.detectChanges();
        Swal.fire('Error', 'No se pudo cargar la lista de pilotos.', 'error');
      }
    });
  }

  cargarBases(): void {
    this.pilotoService.listarBases().subscribe({
      next: (data) => {
        this.bases = data ?? [];
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error bases:', err)
    });
  }

  openForm(): void {
    this.editando = false;
    this.editId = null;
    this.nuevo = this.emptyForm();
    this.showForm = true;
    this.cdr.detectChanges();
  }

  closeForm(): void {
    this.showForm = false;
    this.editando = false;
    this.editId = null;
    this.nuevo = this.emptyForm();
  }

  editar(p: PilotoResponse): void {
    this.editando = true;
    this.editId = p.id;
    this.nuevo = {
      codigo:       p.codigo,
      nombre:       p.nombre,
      licencia:     p.licencia,
      horasDeVuelo: p.horasDeVuelo,
      baseId:       p.baseId ?? undefined,
      estado:       p.estado
    };
    this.showForm = true;
    this.cdr.detectChanges();
  }

  estadoClass(estado: string | null | undefined): string {
    switch (estado) {
      case 'DISPONIBLE': return 'badge badge--green';
      case 'EN_VUELO':   return 'badge badge--blue';
      case 'DESCANSO':   return 'badge badge--orange';
      case 'INACTIVO':   return 'badge badge--red';
      default:           return 'badge badge--gray';
    }
  }

  licenciaClass(licencia: string | null | undefined): string {
    if (!licencia) return 'badge badge--gray';
    return licencia.startsWith('ATPL') ? 'badge badge--blue' : 'badge badge--purple';
  }
}