import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { AvionService, AvionResponse, AvionDTO, BaseResponse } from '../../../../core/services/avion.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-aviones',
    standalone: true,
    imports: [FormsModule, DecimalPipe],
    templateUrl: './aviones.component.html',
    styleUrl: './aviones.component.scss'
})
export class AvionesComponent implements OnInit {

    aviones: AvionResponse[] = [];
    bases: BaseResponse[] = [];
    loading = true;
    showForm = false;
    editando = false;
    editId: number | null = null;
    estados = ['OPERATIVO', 'DISPONIBLE', 'MANTENIMIENTO', 'EN_VUELO', 'INACTIVO'];

    nuevo: Partial<AvionDTO> = {};

    constructor(
        private avionService: AvionService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        this.cargar();
        this.cargarBases();
    }

    cargar(): void {
        this.loading = true;
        this.avionService.listar().subscribe({
            next: (data) => {
                this.aviones = data;
                this.loading = false;
                this.cdr.detectChanges();
            },
            error: () => {
                this.loading = false;
                Swal.fire('Error', 'No se pudo cargar la flota de aviones.', 'error');
            }
        });
    }

    cargarBases(): void {
        this.avionService.listarBases().subscribe({
            next: (data) => this.bases = data,
            error: (err) => console.error('Error cargando bases:', err)
        });
    }

    get total() { return this.aviones.length; }
    get operativos() { return this.aviones.filter(a => a.estado === 'OPERATIVO').length; }
    get mantenimiento() { return this.aviones.filter(a => a.estado === 'MANTENIMIENTO').length; }

    openForm(): void { this.showForm = true; this.editando = false; this.editId = null; this.nuevo = {}; }
    closeForm(): void { this.showForm = false; this.nuevo = {}; }

    editar(a: AvionResponse): void {
        this.editando = true;
        this.editId = a.id;
        this.nuevo = {
            codigo:          a.codigo,
            tipo:            a.tipo,
            capacidad:       a.capacidad,
            horasDeVuelo:    a.horasDeVuelo,
            estado:          a.estado,
            baseId:          a.baseId ?? undefined
        };
        this.showForm = true;
    }

    guardar(): void {
        // Validaciones
        if (!this.nuevo.codigo || !this.nuevo.tipo || !this.nuevo.capacidad) {
            Swal.fire('Campos incompletos', 'Código, tipo y capacidad son obligatorios.', 'warning');
            return;
        }

        if (this.nuevo.capacidad <= 0) {
            Swal.fire('Valor inválido', 'La capacidad debe ser mayor a 0.', 'warning');
            return;
        }

        if (this.nuevo.horasDeVuelo != null && this.nuevo.horasDeVuelo < 0) {
            Swal.fire('Valor inválido', 'Las horas de vuelo no pueden ser negativas.', 'warning');
            return;
        }

        if (this.nuevo.yearFabricacion != null &&
           (this.nuevo.yearFabricacion < 1900 || this.nuevo.yearFabricacion > 2100)) {
            Swal.fire('Valor inválido', 'El año de fabricación debe estar entre 1900 y 2100.', 'warning');
            return;
        }

        const dto: AvionDTO = {
            codigo:          this.nuevo.codigo!,
            tipo:            this.nuevo.tipo!,
            capacidad:       Number(this.nuevo.capacidad),
            horasDeVuelo:    this.nuevo.horasDeVuelo != null ? Number(this.nuevo.horasDeVuelo) : undefined,
            yearFabricacion: this.nuevo.yearFabricacion != null ? Number(this.nuevo.yearFabricacion) : undefined,
            estado:          this.nuevo.estado,
            baseId:          this.nuevo.baseId != null ? Number(this.nuevo.baseId) : undefined
        };

        const operacion = (this.editando && this.editId)
            ? this.avionService.actualizar(this.editId, dto)
            : this.avionService.crear(dto);

        operacion.subscribe({
            next: () => {
                this.closeForm();
                this.cargar();
                Swal.fire({
                    icon: 'success',
                    title: this.editando ? 'Avión Actualizado' : 'Avión Registrado',
                    text: `La aeronave ${dto.codigo} ha sido guardada con éxito.`,
                    timer: 2000,
                    showConfirmButton: false
                });
            },
            error: () => Swal.fire('Error', 'No se pudo procesar la solicitud.', 'error')
        });
    }

    async eliminar(id: number): Promise<void> {
        const result = await Swal.fire({
            title: '¿Confirmar eliminación?',
            text: 'Esta acción desactivará el avión del sistema.',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        });

        if (result.isConfirmed) {
            this.avionService.eliminar(id).subscribe({
                next: () => {
                    this.cargar();
                    Swal.fire('Eliminado', 'El avión ha sido retirado.', 'success');
                },
                error: () => Swal.fire('Error', 'No se pudo eliminar el avión.', 'error')
            });
        }
    }

    toggleMantenimiento(a: AvionResponse): void {
        const enMantenimiento = a.estado === 'MANTENIMIENTO';
        const titulo = enMantenimiento ? '¿Reactivar Avión?' : '¿Enviar a Mantenimiento?';

        Swal.fire({
            title: titulo,
            text: `Se cambiará el estado del avión ${a.codigo}`,
            icon: 'info',
            showCancelButton: true,
            confirmButtonText: 'Confirmar',
            cancelButtonText: 'Volver'
        }).then((result) => {
            if (result.isConfirmed) {
                const obs = enMantenimiento
                    ? this.avionService.reactivar(a.id)
                    : this.avionService.mantenimiento(a.id);

                obs.subscribe({
                    next: () => {
                        this.cargar();
                        Swal.fire('Estado Actualizado', `El avión ahora está ${enMantenimiento ? 'OPERATIVO' : 'EN TALLER'}.`, 'success');
                    },
                    error: () => Swal.fire('Error', 'No se pudo cambiar el estado.', 'error')
                });
            }
        });
    }

    estadoClass(estado: string): string {
        switch (estado) {
            case 'OPERATIVO':    return 'status-op';
            case 'MANTENIMIENTO': return 'status-mant';
            case 'EN_VUELO':     return 'status-flight';
            default:             return 'status-default';
        }
    }
}