import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-cambiar-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cambiar-password.html',
  styleUrl: './cambiar-password.css'
})
export class CambiarPassword {
  token = '';
  password = '';
  confirmarPassword = '';
  mensaje = '';
  error = '';
  cargando = false;

  constructor(private authService: AuthService, private router: Router) {}

  cambiar(): void {
    if (this.password !== this.confirmarPassword) {
      this.error = 'Las contraseñas no coinciden';
      return;
    }
    this.cargando = true;
    this.error = '';
    this.authService.cambiarPassword(this.token, this.password).subscribe({
      next: () => {
        this.mensaje = 'Contraseña cambiada correctamente';
        setTimeout(() => this.router.navigate(['/login']), 2000);
        this.cargando = false;
      },
      error: () => {
        this.error = 'Token inválido o expirado';
        this.cargando = false;
      }
    });
  }
}