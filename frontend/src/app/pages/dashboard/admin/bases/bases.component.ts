import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BaseService, BaseResponse, BaseDTO } from '../../../../core/services/base.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-bases',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './bases.component.html',
  styleUrl: './bases.component.scss'
})
export class BasesComponent implements OnInit {

  bases: BaseResponse[] = [];
  loading = true;
  showForm = false;
  editando = false;
  editId: number | null = null;

  nuevo: Partial<BaseDTO> = this.emptyForm();

  constructor(
    private baseService: BaseService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.cargar(); }

  private emptyForm(): Partial<BaseDTO> {
    return { nombre: '', codigoIata: '', codigoIcao: '', ciudad: '', pais: '', esBaseMantenimiento: false };
  }

  get totalBases() { return this.bases.length; }
  get basesMantenimiento() { return this.bases.filter(b => b.esBaseMantenimiento).length; }
  get basesOperativas() { return this.bases.filter(b => !b.esBaseMantenimiento).length; }

  cargar(): void {
    this.loading = true;
    this.cdr.detectChanges();

    this.baseService.listar().subscribe({
      next: (data) => {
        this.bases = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
        Swal.fire('Error', 'No se pudieron cargar las bases.', 'error');
      }
    });
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

  editar(b: BaseResponse): void {
    this.editando = true;
    this.editId   = b.id;
    this.nuevo    = {
      nombre:              b.nombre,
      codigoIata:          b.codigoIata,
      codigoIcao:          b.codigoIcao,
      ciudad:              b.ciudad,
      pais:                b.pais,
      esBaseMantenimiento: b.esBaseMantenimiento
    };
    this.showForm = true;
    this.cdr.detectChanges();
  }

  private validarCampos(): boolean {
    if (!this.nuevo.nombre?.trim()) {
      Swal.fire('Campo requerido', 'El nombre de la base es obligatorio.', 'warning');
      return false;
    }
    if (!this.nuevo.codigoIata?.trim() || this.nuevo.codigoIata.length !== 3) {
      Swal.fire('Código inválido', 'El código IATA debe tener exactamente 3 caracteres.', 'warning');
      return false;
    }
    if (!this.nuevo.codigoIcao?.trim() || this.nuevo.codigoIcao.length !== 4) {
      Swal.fire('Código inválido', 'El código ICAO debe tener exactamente 4 caracteres.', 'warning');
      return false;
    }
    return true;
  }

  guardar(): void {
    if (!this.validarCampos()) return;

    const dto: BaseDTO = {
      nombre:              this.nuevo.nombre!,
      codigoIata:          this.nuevo.codigoIata!.toUpperCase(),
      codigoIcao:          this.nuevo.codigoIcao!.toUpperCase(),
      ciudad:              this.nuevo.ciudad,
      pais:                this.nuevo.pais,
      esBaseMantenimiento: this.nuevo.esBaseMantenimiento ?? false
    };

    const request$ = this.editando && this.editId
      ? this.baseService.actualizar(this.editId, dto)
      : this.baseService.crear(dto);

    request$.subscribe({
      next: () => {
        Swal.fire(
          this.editando ? '¡Actualizado!' : '¡Registrado!',
          this.editando ? 'Base actualizada correctamente.' : 'Base creada exitosamente.',
          'success'
        );
        this.closeForm();
        this.cargar();
      },
      error: (err) => {
        const msg = typeof err.error === 'string' ? err.error : err.error?.message || 'Error al guardar la base.';
        Swal.fire('Error', msg, 'error');
      }
    });
  }

  eliminar(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'La base será marcada como base de mantenimiento.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, desactivar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.baseService.eliminar(id).subscribe({
          next: () => {
            Swal.fire('Desactivada', 'La base ha sido marcada como mantenimiento.', 'success');
            this.cargar();
          },
          error: () => Swal.fire('Error', 'No se pudo desactivar la base.', 'error')
        });
      }
    });
  }

  reactivar(id: number): void {
    this.baseService.reactivar(id).subscribe({
      next: () => {
        Swal.fire('Reactivada', 'La base está operativa nuevamente.', 'success');
        this.cargar();
      },
      error: () => Swal.fire('Error', 'No se pudo reactivar la base.', 'error')
    });
  }

  tipoClass(b: BaseResponse): string {
    return b.esBaseMantenimiento ? 'badge badge--yellow' : 'badge badge--blue';
  }

  tipoLabel(b: BaseResponse): string {
    return b.esBaseMantenimiento ? 'Mantenimiento' : 'Operativa';
  }
}