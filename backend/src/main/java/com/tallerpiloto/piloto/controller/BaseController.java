package com.tallerpiloto.piloto.controller;

import com.tallerpiloto.piloto.dto.BaseDTO;
import com.tallerpiloto.piloto.model.Base;
import com.tallerpiloto.piloto.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/bases")
@RequiredArgsConstructor
public class BaseController {

    private final BaseService baseService;
    @Transactional
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody BaseDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        try {
           Base base = baseService.crear(dto);
            var uri = uriComponentsBuilder.path("/api/bases/{id}").buildAndExpand(base.getId()).toUri();
            return ResponseEntity.created(uri).body(base);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Base>> listar() {
        return ResponseEntity.ok(baseService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(baseService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody BaseDTO dto) {
        try {
            return ResponseEntity.ok(baseService.actualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            baseService.eliminar(id);
            return ResponseEntity.ok("Base desactivada correctamente correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        try {
            baseService.reactivar(id);
            return ResponseEntity.ok("Base activada correctamente correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





}
