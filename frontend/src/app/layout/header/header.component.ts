import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private authService: AuthService, private router: Router) {}

  get username(): string { return this.authService.getUsername() ?? 'Usuario'; }
  get rol(): string      { return this.authService.getRol() ?? ''; }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}