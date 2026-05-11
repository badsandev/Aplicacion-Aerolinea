import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  username = '';
  password = '';
  error = '';
  cargando = false;
  
  // 1. Declaramos la propiedad que falta
  responseDebug: any = null; 

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.error = '';
    this.cargando = true;
    this.responseDebug = null; // Limpiamos el debug al iniciar

    this.authService.login(this.username, this.password).subscribe({
      next: (res) => {
        // 2. Guardamos la respuesta para verla en el <pre> del HTML
        this.responseDebug = JSON.stringify(res, null, 2);
        
        const token = res?.token ?? res?.accessToken ?? res?.data?.token;
        const username = res?.username ?? res?.user ?? '';
        
        const roleCandidate =
          res?.rol ??
          res?.role ??
          (Array.isArray(res?.roles) ? res.roles[0] : undefined) ??
          (Array.isArray(res?.authorities) ? res.authorities[0]?.authority : undefined) ??
          '';
          
        const rol = typeof roleCandidate === 'string'
          ? roleCandidate.trim().toUpperCase()
          : String(roleCandidate).trim().toUpperCase();

        console.log('Usuario autenticado:', username);
        console.log('Rol detectado:', rol);

        if (!token) {
          this.error = 'No se recibió token de autenticación';
          this.cargando = false;
          return;
        }

        this.authService.guardarSesion(token, rol, username);
        this.cargando = false;

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
          console.log('Navigating to', path);
          this.router.navigateByUrl(path).catch((err) => {
            console.error('Navigation failed', err);
            this.error = 'No se pudo cambiar de página después de iniciar sesión.';
          });
          return;
        }

        this.error = `Rol no reconocido: ${rol || '[vacío]'}`;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Login error', err);
        // 3. También capturamos el error para depuración
        this.responseDebug = JSON.stringify(err, null, 2);
        this.error = 'Usuario o contraseña incorrectos';
        this.cargando = false;
      }
    });
  }
}