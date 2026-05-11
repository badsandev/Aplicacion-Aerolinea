package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.Tripulante;

public record TripulanteResponseDTO(
        Long id,
        String codigo,
        String nombre,
        String rol,
        String base,
        boolean activo
) {
    public static TripulanteResponseDTO fromEntity(Tripulante tripulante) {
        return new TripulanteResponseDTO(
                tripulante.getId(),
                tripulante.getCodigo(),
                tripulante.getNombre(),
                tripulante.getRolTripulante() != null ?
                        tripulante.getRolTripulante().toString() : "Sin rol",
                tripulante.getBase() != null ?
                        tripulante.getBase().getNombre() : "Sin base",
                tripulante.isActivo()
        );
    }
}
