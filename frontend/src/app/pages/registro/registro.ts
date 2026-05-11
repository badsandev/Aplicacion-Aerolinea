import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './registro.html',
  styleUrls: ['./registro.css']
})
export class Registro {
  username = '';
  email = '';
  password = '';
  confirmarPassword = '';
  rol = 'OPERADOR';
  error = '';
  cargando = false;

  nombre = '';
  codigo = '';
  licencia = '';
  horasDeVuelo: number | null = null;
  rolTripulante = '';
  baseId: number | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  registrar(): void {
    if (this.password !== this.confirmarPassword) {
      this.error = 'Las contrasenas no coinciden';
      return;
    }

    this.cargando = true;
    this.error = '';

    const data: any = {
      username: this.username,
      email: this.email,
      password: this.password,
      rol: this.rol
    };

    if (this.rol === 'PILOTO') {
      data.nombre = this.nombre;
      data.codigo = this.codigo;
      data.licencia = this.licencia;
      data.horasDeVuelo = this.horasDeVuelo;
      data.baseId = this.baseId;
    } else if (this.rol === 'TRIPULANTE') {
      data.nombre = this.nombre;
      data.codigo = this.codigo;
      data.rolTripulante = this.rolTripulante;
      data.baseId = this.baseId;
    }

    this.authService.registro(data).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.error = err.error || 'Error al registrar usuario';
        this.cargando = false;
      }
    });
  }
}
