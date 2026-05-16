import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../../core/services/dashboard.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.html',
  styleUrls: ['./admin.scss']
})
export class AdminComponent implements OnInit {

  private destroy$ = new Subject<void>();

  today = new Date();
  loading = true;
  error = false;

  stats = [
    { icon: 'bi-airplane-fill', iconBg: 'blue',   cardBg: 'blue',   value: 0, label: 'Total de Aviones',   delta: 'Cargando...' },
    { icon: 'bi-person-fill',   iconBg: 'green',  cardBg: 'green',  value: 0, label: 'Pilotos Activos',    delta: 'Cargando...' },
    { icon: 'bi-people-fill',   iconBg: 'purple', cardBg: 'purple', value: 0, label: 'Tripulación Total',  delta: 'Cargando...' },
    { icon: 'bi-calendar-fill', iconBg: 'orange', cardBg: 'orange', value: 0, label: 'Vuelos Programados', delta: 'Cargando...' },
  ];

  miniCards = [
    { value: 0, label: 'Vuelos Hoy',      icon: 'bi-clock-fill',         color: '#111827' },
    { value: 0, label: 'En Vuelo Ahora',  icon: 'bi-graph-up-arrow',     color: '#3b82f6' },
    { value: 0, label: 'Completados Hoy', icon: 'bi-check-circle-fill',  color: '#10b981' },
    { value: 0, label: 'Alertas Activas', icon: 'bi-exclamation-circle', color: '#f59e0b' },
  ];

  flights:    any[] = [];
  activities: any[] = [];

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getStats()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data: any) => {
          this.stats[0].value = data.aviones.length;
          this.stats[0].delta = `${data.aviones.length} registrados`;

          this.stats[1].value = data.pilotos.length;
          this.stats[1].delta = `${data.pilotos.length} activos`;

          this.stats[2].value = data.tripulacion.length;
          this.stats[2].delta = `${data.tripulacion.length} miembros`;

          this.stats[3].value = data.vuelos.length;
          this.stats[3].delta = `${data.vuelos.length} en total`;

          const hoy = new Date().toISOString().split('T')[0];
          const vuelosHoy = data.vuelos.filter((v: any) => v.fecha?.startsWith(hoy));

          this.miniCards[0].value = vuelosHoy.length;
          this.miniCards[1].value = data.vuelos.filter((v: any) => v.estado === 'EN_VUELO').length;
          this.miniCards[2].value = data.vuelos.filter((v: any) => v.estado === 'COMPLETADO').length;
          this.miniCards[3].value = data.vuelos.filter((v: any) => v.estado === 'RETRASADO').length;

          this.flights = vuelosHoy.slice(0, 5).map((v: any) => ({
            code:        v.codigo ?? v.id,
            route:       `${v.origen} → ${v.destino}`,
            time:        v.horaSalida ?? v.fecha,
            statusLabel: v.estado,
            statusClass: this.estadoClass(v.estado)
          }));

          this.loading = false;
          this.error   = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('❌ ERROR DASHBOARD:', err);
          this.loading = false;
          this.error   = true;
          this.cdr.detectChanges();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private estadoClass(estado: string): string {
    const map: Record<string, string> = {
      'EN_VUELO':   'badge--green',
      'COMPLETADO': 'badge--blue',
      'RETRASADO':  'badge--yellow',
      'CANCELADO':  'badge--red',
      'PROGRAMADO': 'badge--gray',
    };
    return map[estado] ?? 'badge--gray';
  }
}