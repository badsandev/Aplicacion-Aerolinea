import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { Registro } from './pages/registro/registro';
import { AdminComponent } from './pages/dashboard/admin/admin.component';
import { PilotoComponent } from './pages/dashboard/piloto/piloto.component';
import { Tripulante } from './pages/dashboard/tripulante/tripulante';
import { OperadorComponent } from './pages/dashboard/operador/operador.component';
import { authGuard } from './core/guards/auth.guard';
import { RecuperarPassword } from './pages/recuperar-password/recuperar-password';
import { CambiarPassword } from './pages/cambiar-password/cambiar-password';
import { RouterModule, Router } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: Registro },
  { path: 'recuperar-password', component: RecuperarPassword },
  { path: 'cambiar-password', component: CambiarPassword },
  { path: 'admin/dashboard', component: AdminComponent, canActivate: [authGuard] },
  { path: 'piloto/dashboard', component: PilotoComponent, canActivate: [authGuard] },
  { path: 'tripulante/dashboard', component: Tripulante, canActivate: [authGuard] },
  { path: 'operador/dashboard', component: OperadorComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'login' }
];