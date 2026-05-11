package com.tallerpiloto.piloto.controller;

import com.tallerpiloto.piloto.dto.TripulanteDTO;
import com.tallerpiloto.piloto.dto.TripulanteResponseDTO;
import com.tallerpiloto.piloto.model.Tripulante;
import com.tallerpiloto.piloto.service.TripulanteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/tripulantes")
@RequiredArgsConstructor
public class TripulanteController {


    private final TripulanteService tripulanteService;


    @Transactional
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TripulanteDTO dto,
                                   UriComponentsBuilder uriComponentsBuilder) {
        try {
            TripulanteResponseDTO tripulante = tripulanteService.crear(dto);
            var uri = uriComponentsBuilder.path("/api/tripulantes/{id}")
                    .buildAndExpand(tripulante.id()).toUri();
            return ResponseEntity.created(uri).body(tripulante);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<TripulanteResponseDTO>> listar() {
        return ResponseEntity.ok(tripulanteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tripulanteService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                         @RequestBody TripulanteDTO dto) {
        try {
            return ResponseEntity.ok(tripulanteService.actualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            tripulanteService.eliminar(id);
            return ResponseEntity.ok("Tripulante desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        try {
            tripulanteService.reactivar(id);
            return ResponseEntity.ok("Tripulante reactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
