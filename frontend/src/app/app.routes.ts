import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { Registro } from './pages/registro/registro';
import { RecuperarPassword } from './pages/recuperar-password/recuperar-password';
import { CambiarPassword } from './pages/cambiar-password/cambiar-password';
import { AdminComponent } from './pages/dashboard/admin/admin.component';
import { PilotoComponent } from './pages/dashboard/piloto/piloto.component';
import { Tripulante } from './pages/dashboard/tripulante/tripulante';
import { OperadorComponent } from './pages/dashboard/operador/operador.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './core/guards/auth.guard';
import { AvionesComponent } from './pages/dashboard/admin/aviones/aviones.component';
import { PilotosComponent } from './pages/dashboard/admin/pilotos/pilotos.component';
import { UsuariosComponent } from './pages/dashboard/admin/usuarios/usuarios.component'
import { TripulacionComponent } from './pages/dashboard/admin/tripulacion/tripulacion.component';
import { BasesComponent } from './pages/dashboard/admin/bases/bases.component';
import { VuelosComponent } from './pages/dashboard/admin/vuelos/vuelos.component';
import { AsignacionComponent } from './pages/dashboard/admin/asignacion/asignacion.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: Registro },
  { path: 'recuperar-password', component: RecuperarPassword },
  { path: 'cambiar-password', component: CambiarPassword },
  {
    path: 'admin',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: AdminComponent },
      { path: 'aviones', component: AvionesComponent },
      { path: 'pilotos',   component: PilotosComponent },
      { path: 'usuarios' , component: UsuariosComponent},
      { path: 'tripulacion', component: TripulacionComponent },
      { path: 'bases', component: BasesComponent },
      { path: 'vuelos', component: VuelosComponent },
      { path: 'asignacion', component: AsignacionComponent },

    ]
  },
  {
    path: 'piloto',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: PilotoComponent },
    ]
  },
  {
    path: 'tripulante',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: Tripulante },
    ]
  },
  {
    path: 'operador',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: OperadorComponent },
    ]
  },
  { path: '**', redirectTo: 'login' }
];