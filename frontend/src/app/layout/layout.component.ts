import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HeaderComponent } from './header/header.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, HeaderComponent],
  template: `
    <div class="app-shell">
      <app-sidebar />
      <div class="app-shell__main">
        <app-header />
        <main class="app-shell__content">
          <router-outlet />
        </main>
      </div>
    </div>
  `,
  styles: [`
    .app-shell {
      display: flex;
      height: 100vh;
      overflow: hidden;
    }
    .app-shell__main {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;
    }
    .app-shell__content {
      flex: 1;
      overflow-y: auto;
      padding: 24px;
      background: #f0f2f5;
    }
  `]
})
export class LayoutComponent {}