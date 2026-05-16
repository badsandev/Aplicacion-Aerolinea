import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import {
  TripulanteService,
  TripulanteResponse,
  TripulanteDTO,
  RolTripulante,
  EstadoTripulante
} from '../../../../core/services/tripulante.service';

import { BaseResponse } from '../../../../core/services/avion.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-tripulacion',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './tripulacion.component.html',
  styleUrls: ['./tripulacion.component.scss']
})
export class TripulacionComponent implements OnInit {

  tripulantes: TripulanteResponse[] = [];
  bases: BaseResponse[] = [];

  loading = true;
  showForm = false;
  editando = false;
  editId: number | null = null;

  roles  = Object.values(RolTripulante);
  estados = Object.values(EstadoTripulante);

  nuevo: Partial<TripulanteDTO> = this.getEmptyForm();

  constructor(
    private tripulanteService: TripulanteService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.cargarBases();
  }

  private getEmptyForm(): Partial<TripulanteDTO> {
    return {
      rol:      RolTripulante.AZAFATA,
      estado:   EstadoTripulante.DISPONIBLE,
      username: '',
      email:    '',
      password: '',
      codigo:   '',
      nombre:   '',
      baseId:   undefined
    };
  }

  cargar(): void {
    this.loading = true;
    this.cdr.detectChanges();

    this.tripulanteService.listar().subscribe({
      next: (data) => {
        this.tripulantes = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR LISTANDO TRIPULANTES:', err);
        this.loading = false;
        this.cdr.detectChanges();
        Swal.fire('Error', 'No se pudieron cargar los tripulantes.', 'error');
      }
    });
  }

  cargarBases(): void {
    this.tripulanteService.listarBases().subscribe({
      next: (data) => {
        this.bases = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error cargando bases:', err)
    });
  }

  get total(): number    { return this.tripulantes.length; }
  get activos(): number  { return this.tripulantes.filter(t => t.activo).length; }
  get jefes(): number    { return this.tripulantes.filter(t => t.rol === RolTripulante.SOBRECARGO).length; }
  get auxiliares(): number { return this.tripulantes.filter(t => t.rol === RolTripulante.AZAFATA).length; }

  openForm(): void {
    this.editando = false;
    this.editId   = null;
    this.showForm = true;
    this.nuevo    = this.getEmptyForm();
    this.cdr.detectChanges();
  }

  closeForm(): void {
    this.showForm = false;
    this.editando = false;
    this.editId   = null;
    this.nuevo    = this.getEmptyForm();
  }

  editar(t: TripulanteResponse): void {
    this.editando = true;
    this.editId   = t.id;
    this.showForm = true;
    this.nuevo = {
      codigo: t.codigo,
      nombre: t.nombre,
      rol:    t.rol,
      estado: t.estado,
      baseId: t.baseId ?? undefined
    };
    this.cdr.detectChanges();
  }

  private validarCampos(): boolean {
    const soloLetras = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!this.nuevo.codigo?.trim()) {
      Swal.fire('Campo requerido', 'El código de tripulante es obligatorio.', 'warning');
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
    if (!this.nuevo.rol) {
      Swal.fire('Campo requerido', 'Debe seleccionar un rol.', 'warning');
      return false;
    }
    if (!this.nuevo.estado) {
      Swal.fire('Campo requerido', 'Debe seleccionar un estado personal.', 'warning');
      return false;
    }

    if (!this.editando) {
      if (!this.nuevo.username?.trim()) {
        Swal.fire('Campo requerido', 'El nombre de usuario es obligatorio.', 'warning');
        return false;
      }
      if (!this.nuevo.email?.trim() || !emailRegex.test(this.nuevo.email)) {
        Swal.fire('Correo inválido', 'Ingrese un correo electrónico válido.', 'warning');
        return false;
      }
      if (!this.nuevo.password || this.nuevo.password.length < 6) {
        Swal.fire('Contraseña inválida', 'La contraseña debe tener mínimo 6 caracteres.', 'warning');
        return false;
      }
    }

    return true;
  }

  guardar(): void {
    if (!this.validarCampos()) return;

    const baseDto: Omit<TripulanteDTO, 'username' | 'email' | 'password'> = {
      codigo: this.nuevo.codigo!,
      nombre: this.nuevo.nombre!,
      rol:    this.nuevo.rol,
      estado: this.nuevo.estado,
      baseId: this.nuevo.baseId != null ? Number(this.nuevo.baseId) : undefined
    };

    const dto: TripulanteDTO = this.editando
      ? baseDto
      : { ...baseDto, username: this.nuevo.username, email: this.nuevo.email, password: this.nuevo.password };

    const request$ = this.editando && this.editId
      ? this.tripulanteService.actualizar(this.editId, dto)
      : this.tripulanteService.crear(dto);

    request$.subscribe({
      next: () => {
        Swal.fire(
          this.editando ? 'Actualizado' : 'Registrado',
          this.editando ? 'Tripulante actualizado correctamente.' : 'Tripulante creado exitosamente.',
          'success'
        );
        this.closeForm();
        this.cargar();
      },
      error: (err) => {
        const msg = typeof err.error === 'string' ? err.error : err.error?.message || 'Error al guardar el tripulante.';
        Swal.fire('Error', msg, 'error');
      }
    });
  }

  eliminar(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'El tripulante será marcado como inactivo.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#10b981',
      cancelButtonColor: '#ef4444',
      confirmButtonText: 'Sí, desactivar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.tripulanteService.eliminar(id).subscribe({
          next: () => {
            Swal.fire('Desactivado', 'El tripulante ahora está inactivo.', 'success');
            this.cargar();
          },
          error: () => Swal.fire('Error', 'No se pudo desactivar el tripulante.', 'error')
        });
      }
    });
  }

  reactivar(id: number): void {
    this.tripulanteService.reactivar(id).subscribe({
      next: () => {
        Swal.fire('Reactivado', 'El tripulante vuelve a estar activo.', 'success');
        this.cargar();
      },
      error: () => Swal.fire('Error', 'No se pudo reactivar el tripulante.', 'error')
    });
  }

  initials(nombre: string): string {
    return nombre.split(' ').slice(0, 2).map(n => n[0]).join('').toUpperCase();
  }

  rolClass(rol: string): string {
    switch (rol) {
      case 'SOBRECARGO': return 'badge badge--purple';
      case 'AZAFATA':    return 'badge badge--blue';
      case 'COPILOTO':   return 'badge badge--green';
      case 'TECNICO':    return 'badge badge--orange';
      case 'SEGURIDAD':  return 'badge badge--red';
      case 'MEDICO':     return 'badge badge--violet';
      default:           return 'badge';
    }
  }

  estadoClass(estado: string): string {
    switch (estado) {
      case 'DISPONIBLE': return 'badge badge--green';
      case 'EN_VUELO':   return 'badge badge--blue';
      case 'DESCANSO':   return 'badge badge--yellow';
      case 'INACTIVO':   return 'badge badge--red';
      default:           return 'badge';
    }
  }

  estadoLabel(estado: string | null | undefined): string {
    return estado ?? 'SIN ESTADO';
  }
}