package com.tallerpiloto.piloto.controller;

import com.tallerpiloto.piloto.dto.UsuarioDTO;
import com.tallerpiloto.piloto.dto.UsuarioResponseDTO;
import com.tallerpiloto.piloto.model.Usuario;
import com.tallerpiloto.piloto.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Transactional
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody UsuarioDTO dto) {
        Usuario usuario = usuarioService.registar(dto);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody UsuarioDTO dto) {
        try {
            return ResponseEntity.ok(usuarioService.actualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.ok("Usuario desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        try {
            usuarioService.reactivar(id);
            return ResponseEntity.ok("Usuario reactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}