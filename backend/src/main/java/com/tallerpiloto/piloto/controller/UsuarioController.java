package com.tallerpiloto.piloto.controller;

import com.tallerpiloto.piloto.dto.UsuarioDTO;
import com.tallerpiloto.piloto.model.Usuario;
import com.tallerpiloto.piloto.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuario) {
        this.usuarioService = usuario;
    }
    @Transactional
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody UsuarioDTO dto, UriComponentsBuilder uriComponentsBuilder ) {
        try{
            Usuario usuario = usuarioService.registar(dto);
            var uri = uriComponentsBuilder.path("/api/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
            return ResponseEntity.created(uri).body(usuario);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        try{
            return ResponseEntity.ok(usuarioService.actualizar(id, dto));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @DeleteMapping
    public ResponseEntity<?> eliminar(@RequestParam Long id) {
        try{
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
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
