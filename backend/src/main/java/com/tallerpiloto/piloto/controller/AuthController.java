package com.tallerpiloto.piloto.controller;

import com.tallerpiloto.piloto.dto.LoginDTO;
import com.tallerpiloto.piloto.dto.TokenDTO;
import com.tallerpiloto.piloto.dto.UsuarioDTO;
import com.tallerpiloto.piloto.model.Usuario;
import com.tallerpiloto.piloto.security.UsuarioDetailsService;
import com.tallerpiloto.piloto.service.JwtService;
import com.tallerpiloto.piloto.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            // valida credenciales contra la base de datos
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.username(), dto.password())
            );

            UserDetails userDetails = usuarioDetailsService
                    .loadUserByUsername(dto.username());

            String token = jwtService.generarToken(userDetails);
            String rol = userDetails.getAuthorities()
                    .iterator().next().getAuthority();

            return ResponseEntity.ok(new TokenDTO(token, rol, dto.username()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody UsuarioDTO dto) {
        try {
            Usuario usuario = usuarioService.registar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<?> recuperarPassword(@RequestBody Map<String, String> body) {
        try {
            usuarioService.solicitarRecuperar(body.get("email"));
            return ResponseEntity.ok("Email de recuperación enviado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody Map<String, String> body) {
        try {
            usuarioService.cambiarPassword(body.get("token"), body.get("password"));
            return ResponseEntity.ok("Contraseña cambiada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
