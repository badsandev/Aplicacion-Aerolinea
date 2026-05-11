import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router'; 
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-recuperar-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './recuperar-password.html',
  styleUrl: './recuperar-password.css'
})
export class RecuperarPassword {
  email = '';
  mensaje = '';
  error = '';
  cargando = false;

  constructor(private authService: AuthService, private router: Router) {}

 recuperar(): void {
    this.cargando = true;
    this.error = '';
    this.mensaje = '';
    this.authService.recuperarPassword(this.email).subscribe({
      next: () => {
        this.cargando = false;
        console.log('Email enviado, navegando...'); // ← agregar
        this.router.navigate(['/cambiar-password']).then(result => {
            console.log('Navegación resultado:', result); // ← agregar
        });
      },
      error: () => {
        this.error = 'Email no registrado';
        this.cargando = false;
      }
    });
  }
}