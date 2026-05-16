package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.Vuelo;

import java.time.LocalDateTime;
import java.util.List;

public record VueloResponseDTO(
        Long id,
        String numVuelo,
        String origen,
        Long origenId,
        String destino,
        Long destinoId,
        LocalDateTime fechaHoraSalida,
        LocalDateTime fechaHoraLlegada,
        String estado,
        String avion,
        Long avionId,
        String piloto,
        Long pilotoId,
        List<String> tripulacion,
        List<Long> tripulacionIds) {
    public static VueloResponseDTO fromEntity(Vuelo vuelo) {
        return new VueloResponseDTO(
                vuelo.getId(),
                vuelo.getNumVuelo(),
                vuelo.getOrigen() != null ? vuelo.getOrigen().getNombre() : "Sin origen",
                vuelo.getOrigen() != null ? vuelo.getOrigen().getId() : null,
                vuelo.getDestino() != null ? vuelo.getDestino().getNombre() : "Sin destino",
                vuelo.getDestino() != null ? vuelo.getDestino().getId() : null,
                vuelo.getFechaHoraSalida(),
                vuelo.getFechaHoraLlegada(),
                vuelo.getEstado().toString(),
                vuelo.getAvion() != null ? vuelo.getAvion().getCodigo() : "Sin avion",
                vuelo.getAvion() != null ? vuelo.getAvion().getId() : null,
                vuelo.getPiloto() != null ? vuelo.getPiloto().getNombre() : "Sin piloto",
                vuelo.getPiloto() != null ? vuelo.getPiloto().getId() : null,
                vuelo.getTripulacion().stream()
                        .map(t -> t.getNombre())
                        .toList(),
                vuelo.getTripulacion().stream()
                        .map(t -> t.getId()).toList()
        );
    }
}