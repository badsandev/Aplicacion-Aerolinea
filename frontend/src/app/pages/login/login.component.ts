import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import Swal from 'sweetalert2'; 

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './login.component.html'
  
})
export class LoginComponent {

  username = '';
  password = '';
  cargando = false;
  responseDebug: any = null; 

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
   
    if (this.cargando) return;

    this.cargando = true;
    this.responseDebug = null;

    this.authService.login(this.username, this.password).subscribe({
      next: (res) => {
        this.responseDebug = JSON.stringify(res, null, 2);
        
        // Extracción flexible de Token, Usuario y Roles
        const token = res?.token ?? res?.accessToken ?? res?.data?.token;
        const username = res?.username ?? res?.user ?? this.username;
        
        const roleCandidate =
          res?.rol ??
          res?.role ??
          (Array.isArray(res?.roles) ? res.roles[0] : undefined) ??
          (Array.isArray(res?.authorities) ? res.authorities[0]?.authority : undefined) ??
          '';
          
        const rol = String(roleCandidate).trim().toUpperCase();

        if (!token) {
          this.cargando = false;
          Swal.fire('Error de Autenticación', 'Servidor no proporcionó un token.', 'error');
          return;
        }

       
        this.authService.guardarSesion(token, rol, username);

        // Selección de ruta según el rol
        let path: string | null = null;
        if (rol.includes('ADMIN')) {
          path = '/admin/dashboard';
        } else if (rol.includes('PILOTO')) {
          path = '/piloto/dashboard';
        } else if (rol.includes('TRIPULANTE')) {
          path = '/tripulante/dashboard';
        } else if (rol.includes('OPERADOR')) {
          path = '/operador/dashboard';
        }

        if (path) {
          this.cargando = false;
          
          
          Swal.fire({
            icon: 'success',
            title: `¡Bienvenido, ${username}!`,
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true
          });

          this.router.navigateByUrl(path).catch((err) => {
            console.error('Error al navegar:', err);
            Swal.fire('Error', 'La ruta de destino no existe.', 'error');
          });
        } else {
          this.cargando = false;
          Swal.fire('Acceso Denegado', `El rol [${rol}] no tiene permisos de acceso.`, 'warning');
        }
      },
      error: (err) => {
        console.error('Error en el login:', err);
        this.cargando = false;
        this.responseDebug = JSON.stringify(err, null, 2);
        
        Swal.fire({
          icon: 'error',
          title: 'Fallo al ingresar',
          text: 'Usuario o contraseña no válidos.',
          confirmButtonColor: '#3085d6'
        });
      }
    });
  }
}