package com.tallerpiloto.piloto.controller;


import com.tallerpiloto.piloto.dto.PilotoDTO;
import com.tallerpiloto.piloto.dto.PilotoResponseDTO;
import com.tallerpiloto.piloto.model.Piloto;
import com.tallerpiloto.piloto.service.PilotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/pilotos")
@RequiredArgsConstructor
public class PilotoController {

    private final PilotoService pilotoService;

    @Transactional
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PilotoDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        try{
            Piloto piloto = pilotoService.crearPiloto(dto);
            var uri = uriComponentsBuilder.path("/api/pilotos/{id}")
                    .buildAndExpand(piloto.getId()).toUri();
            return ResponseEntity.created(uri).body(PilotoResponseDTO.fromEntity(piloto));

        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
    @GetMapping
    public ResponseEntity<List<PilotoResponseDTO>> listar() {
        return ResponseEntity.ok(pilotoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try{
            return ResponseEntity.ok(pilotoService.buscarPilotoPorId(id));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody PilotoDTO dto) {
        try {
            return ResponseEntity.ok(pilotoService.actualizarPiloto(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try{
            pilotoService.eliminar(id);
            return ResponseEntity.ok().body("Piloto eliminado correctamente");
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivar(@PathVariable Long id){
        try{
            pilotoService.reactivar(id);
            return ResponseEntity.ok("Piloto reactivado exitosamente");
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }





}
