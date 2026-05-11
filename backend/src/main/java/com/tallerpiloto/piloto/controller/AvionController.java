package com.tallerpiloto.piloto.controller;

import com.tallerpiloto.piloto.dto.AvionDTO;
import com.tallerpiloto.piloto.dto.AvionResponseDTO;
import com.tallerpiloto.piloto.model.Avion;
import com.tallerpiloto.piloto.service.AvionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/aviones")
@RequiredArgsConstructor
public class AvionController {

    private final AvionService avionService;

    @Transactional
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody AvionDTO dto,
                                   UriComponentsBuilder uriComponentsBuilder) {
        try {
            AvionResponseDTO avion = avionService.crear(dto);
            var uri = uriComponentsBuilder.path("/api/aviones/{id}")
                    .buildAndExpand(avion.id()).toUri();
            return ResponseEntity.created(uri).body(avion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AvionResponseDTO>> listar() {
        return ResponseEntity.ok(avionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(avionService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                         @RequestBody AvionDTO dto) {
        try {
            return ResponseEntity.ok(avionService.actualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            avionService.eliminar(id);
            return ResponseEntity.ok("Avion desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Transactional
    @PutMapping("/{id}/reactivar")
    public  ResponseEntity<?> reactivar(@PathVariable Long id) {
        try {
            avionService.reactivar(id);
            return ResponseEntity.ok("Avion reactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Transactional
    @PutMapping("/{id}/mantenimiento")
    public ResponseEntity<?> registrarMantenimiento(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(avionService.registrarMantenimiento(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





}
