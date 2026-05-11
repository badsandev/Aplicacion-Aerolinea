package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.Vuelo;

import java.time.LocalDateTime;
import java.util.List;

public record VueloResponseDTO<LocalDateTime>(
        Long id,
        String numVuelo,
        String origen,
        String destino,
        LocalDateTime fechaHoraSalida,
        LocalDateTime fechaHoraLlegada,
        String estado,
        String avion,
        String piloto,
        List<String> tripulacion
) {
    public static VueloResponseDTO fromEntity(Vuelo vuelo) {
        return new VueloResponseDTO(
                vuelo.getId(),
                vuelo.getNumVuelo(),
                vuelo.getOrigen() != null ? vuelo.getOrigen().getNombre(): "Sin origen",
                vuelo.getDestino() != null ? vuelo.getDestino().getNombre() : "Sin destino",
                vuelo.getFechaHoraSalida(),
                vuelo.getFechaHoraLlegada(),
                vuelo.getEstado().toString(),
                vuelo.getAvion() != null ? vuelo.getAvion().getCodigo() : "Sin avion",
                vuelo.getPiloto() != null ? vuelo.getPiloto().getNombre() : "Sin piloto",
                vuelo.getTripulacion().stream()
                        .map(t -> t.getNombre())
                        .toList()
        );
    }
}