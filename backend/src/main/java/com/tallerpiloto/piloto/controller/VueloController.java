package com.tallerpiloto.piloto.controller;

import com.tallerpiloto.piloto.dto.VueloDTO;
import com.tallerpiloto.piloto.dto.VueloResponseDTO;
import com.tallerpiloto.piloto.model.EstadoVuelo;
import com.tallerpiloto.piloto.model.Vuelo;
import com.tallerpiloto.piloto.service.VueloService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vuelos")
@RequiredArgsConstructor
public class VueloController {

    private final VueloService vueloService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody VueloDTO dto,
                                   UriComponentsBuilder uriComponentsBuilder) {
        try {
            Vuelo vuelo = vueloService.crear(dto);
            var uri = uriComponentsBuilder.path("/api/vuelos/{id}")
                    .buildAndExpand(vuelo.getId()).toUri();
            return ResponseEntity.created(uri).body(VueloResponseDTO.fromEntity(vuelo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<VueloResponseDTO>> listar() {
        return ResponseEntity.ok(vueloService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vueloService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VueloResponseDTO>> listarPorEstado(
            @PathVariable EstadoVuelo estado) {
        return ResponseEntity.ok(vueloService.listarPorEstado(estado));
    }

    @GetMapping("/piloto/{pilotoId}")
    public ResponseEntity<List<VueloResponseDTO>> listarPorPiloto(
            @PathVariable Long pilotoId) {
        return ResponseEntity.ok(vueloService.listarPorPiloto(pilotoId));
    }

    @GetMapping("/tripulante/{tripulanteId}")
    public ResponseEntity<List<VueloResponseDTO>> listarPorTripulante(
            @PathVariable Long tripulanteId) {
        return ResponseEntity.ok(vueloService.listarPorTripulante(tripulanteId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody VueloDTO dto) {
        try {
            return ResponseEntity.ok(vueloService.actualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            vueloService.cancelar(id);
            return ResponseEntity.ok("Vuelo cancelado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PatchMapping("/{id}/retrasar")
    public ResponseEntity<?> retrasar(@PathVariable Long id,
                                      @RequestBody Map<String, String> body) {
        try {
            LocalDateTime nuevaFecha = LocalDateTime.parse(body.get("nuevaFechaSalida"));
            vueloService.retrasar(id, nuevaFecha);
            return ResponseEntity.ok("Vuelo retrasado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/aterrizar")
    public ResponseEntity<?> aterrizar(@PathVariable Long id) {
        try {
            Vuelo vuelo = vueloService.aterrizar(id);
            return ResponseEntity.ok(VueloResponseDTO.fromEntity(vuelo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
