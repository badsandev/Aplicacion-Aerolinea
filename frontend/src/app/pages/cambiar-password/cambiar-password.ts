import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import Swal from 'sweetalert2'; // Importamos SweetAlert

@Component({
  selector: 'app-cambiar-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cambiar-password.html'
  // styleUrl eliminado para evitar errores de compilación por archivo inexistente
})
export class CambiarPassword {
  token = '';
  password = '';
  confirmarPassword = '';
  cargando = false;

  constructor(private authService: AuthService, private router: Router) {}

  cambiar(): void {
    // 1. Validación de coincidencia
    if (this.password !== this.confirmarPassword) {
      Swal.fire({
        icon: 'warning',
        title: 'Contraseñas diferentes',
        text: 'Asegúrate de que ambas contraseñas sean iguales.',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    this.cargando = true;

    this.authService.cambiarPassword(this.token, this.password).subscribe({
      next: () => {
        // 2. Notificación de éxito
        Swal.fire({
          icon: 'success',
          title: '¡Contraseña actualizada!',
          text: 'Tu acceso ha sido restaurado. Redirigiendo...',
          showConfirmButton: false,
          timer: 2000
        });

        setTimeout(() => {
          this.cargando = false;
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.cargando = false;
        console.error('Error al cambiar pass:', err);
        
        
        Swal.fire({
          icon: 'error',
          title: 'Token no válido',
          text: 'El código de seguridad ha expirado o es incorrecto.',
          confirmButtonColor: '#d33'
        });
      }
    });
  }
}