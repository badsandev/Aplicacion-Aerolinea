import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService, UsuarioResponse, UsuarioDTO } from '../../../../core/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss'
})
export class UsuariosComponent implements OnInit {

  usuarios: UsuarioResponse[] = [];
  loading = true;
  showForm = false;
  editando = false;
  editId: number | null = null;
  busqueda = '';

  roles = ['ADMIN', 'OPERADOR', 'PILOTO', 'TRIPULANTE'];
  nuevo: Partial<UsuarioDTO> = {};

  constructor(
    private usuarioService: UsuarioService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading = true;
    this.usuarioService.listar().subscribe({
      next: (data: any) => {
        // Manejo de respuesta: detecta si es un array directo o viene en un campo 'content'
        if (Array.isArray(data)) {
          this.usuarios = data;
        } else if (data && data.content) {
          this.usuarios = data.content;
        } else {
          this.usuarios = [];
        }
        
        this.loading = false;
        this.cdr.detectChanges(); // Asegura que la vista se entere del cambio
      },
      error: (err) => {
        this.loading = false;
        console.error('Error al cargar usuarios:', err);
        Swal.fire('Error', 'No se pudieron cargar los usuarios del servidor.', 'error');
      }
    });
  }

  get total()     { return this.usuarios.length; }
  get activos()   { return this.usuarios.filter(u => u.activo).length; }
  get inactivos() { return this.usuarios.filter(u => !u.activo).length; }

  get usuariosFiltrados(): UsuarioResponse[] {
    const b = this.busqueda.toLowerCase().trim();
    if (!b) return this.usuarios;
    return this.usuarios.filter(u =>
      u.username.toLowerCase().includes(b) ||
      u.email.toLowerCase().includes(b) ||
      (u.nombre && u.nombre.toLowerCase().includes(b)) ||
      (u.codigo && u.codigo.toLowerCase().includes(b))
    );
  }

  rolClass(rol: string): string {
    switch (rol) {
      case 'ADMIN':      return 'badge badge--red';
      case 'PILOTO':     return 'badge badge--blue';
      case 'TRIPULANTE': return 'badge badge--purple';
      case 'OPERADOR':   return 'badge badge--green';
      default:           return 'badge';
    }
  }

  openForm(): void  { 
    this.showForm = true; 
    this.editando = false; 
    this.editId = null; 
    this.nuevo = { rol: 'OPERADOR' }; 
  }

  closeForm(): void { 
    this.showForm = false; 
    this.nuevo = {}; 
  }

  editar(u: UsuarioResponse): void {
    this.editando = true;
    this.editId   = u.id;
    this.nuevo    = {
      username: u.username,
      email:    u.email,
      rol:      u.rol,
      nombre:   u.nombre || '',
      codigo:   u.codigo || ''
    };
    this.showForm = true;
  }

  guardar(): void {
    if (!this.nuevo.username || !this.nuevo.email || !this.nuevo.rol) {
      Swal.fire('Campos incompletos', 'Usuario, Email y Rol son obligatorios.', 'warning');
      return;
    }

    const dto: UsuarioDTO = {
      username: this.nuevo.username!,
      email:    this.nuevo.email!,
      password: this.nuevo.password,
      rol:      this.nuevo.rol!,
      nombre:   this.nuevo.nombre,
      codigo:   this.nuevo.codigo,
      baseId:   this.nuevo.baseId,
      licencia: this.nuevo.licencia,
      horasDeVuelo: this.nuevo.horasDeVuelo,
      rolTripulante: this.nuevo.rolTripulante
    };

    if (this.editando && this.editId) {
      this.usuarioService.actualizar(this.editId, dto).subscribe({
        next: () => {
          Swal.fire('¡Actualizado!', 'Usuario actualizado correctamente.', 'success');
          this.closeForm();
          this.cargar();
        },
        error: (err) => Swal.fire('Error', err.error || 'No se pudo actualizar.', 'error')
      });
    } else {
      if (!this.nuevo.password) {
        Swal.fire('Atención', 'La contraseña es obligatoria para nuevos usuarios.', 'warning');
        return;
      }
      this.usuarioService.crear(dto).subscribe({
        next: () => {
          Swal.fire('¡Registrado!', 'Usuario creado correctamente.', 'success');
          this.closeForm();
          this.cargar();
        },
        error: (err) => Swal.fire('Error', err.error || 'No se pudo crear el usuario.', 'error')
      });
    }
  }

  eliminar(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'El usuario será marcado como inactivo.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#10b981',
      cancelButtonColor: '#ef4444',
      confirmButtonText: 'Sí, desactivar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.usuarioService.eliminar(id).subscribe({
          next: () => {
            Swal.fire('Desactivado', 'El usuario ha sido desactivado.', 'success');
            this.cargar();
          },
          error: () => Swal.fire('Error', 'No se pudo realizar la desactivación.', 'error')
        });
      }
    });
  }

  reactivar(id: number): void {
    this.usuarioService.reactivar(id).subscribe({
      next: () => {
        Swal.fire('Reactivado', 'El usuario está activo nuevamente.', 'success');
        this.cargar();
      },
      error: () => Swal.fire('Error', 'No se pudo reactivar el usuario.', 'error')
    });
  }
}