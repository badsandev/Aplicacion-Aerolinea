import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

interface NavItem { label: string; icon: string; route: string; }

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {

  constructor(private authService: AuthService) {}

  get rol(): string {
    return this.authService.getRol() ?? '';
  }

  get navItems(): NavItem[] {
    if (this.rol.includes('ADMIN')) {
      return [
        { label: 'Dashboard',    icon: 'bi-grid-1x2-fill',      route: '/admin/dashboard'   },
        { label: 'Aviones',      icon: 'bi-airplane-fill',       route: '/admin/aviones'     },
        { label: 'Pilotos',      icon: 'bi-person-check-fill',   route: '/admin/pilotos'     },
        { label: 'Tripulación',  icon: 'bi-people-fill',         route: '/admin/tripulacion' },
        { label: 'Vuelos',       icon: 'bi-calendar-event-fill', route: '/admin/vuelos'      },
        { label: 'Asignaciones', icon: 'bi-person-plus-fill',    route: '/admin/asignacion'  },
        { label: 'Bases',        icon: 'bi-building-fill',       route: '/admin/bases'       },
        { label: 'Usuarios',     icon: 'bi-people-fill',         route: '/admin/usuarios'    },
      ];
    }
    if (this.rol.includes('PILOTO')) {
      return [
        { label: 'Mi Dashboard', icon: 'bi-grid-1x2-fill',      route: '/piloto/dashboard'  },
        { label: 'Mis Vuelos',   icon: 'bi-calendar-event-fill', route: '/piloto/vuelos'     },
      ];
    }
    if (this.rol.includes('TRIPULANTE')) {
      return [
        { label: 'Mi Dashboard', icon: 'bi-grid-1x2-fill',      route: '/tripulante/dashboard' },
        { label: 'Mis Vuelos',   icon: 'bi-calendar-event-fill', route: '/tripulante/vuelos'    },
      ];
    }
    if (this.rol.includes('OPERADOR')) {
      return [
        { label: 'Mi Dashboard', icon: 'bi-grid-1x2-fill',      route: '/operador/dashboard' },
        { label: 'Vuelos',       icon: 'bi-calendar-event-fill', route: '/operador/vuelos'    },
      ];
    }
    return [];
  }
}