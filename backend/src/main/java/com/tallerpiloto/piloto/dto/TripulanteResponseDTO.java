package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.Tripulante;

public record TripulanteResponseDTO(

        Long id,
        String codigo,
        String nombre,
        String rol,
        String estado,
        String base,
        Long baseId,
        boolean activo

) {
    public static TripulanteResponseDTO fromEntity(Tripulante t) {
        return new TripulanteResponseDTO(
                t.getId(),
                t.getCodigo(),
                t.getNombre(),
                t.getRolTripulante() != null ? t.getRolTripulante().toString() : "Sin rol",
                t.getEstado() != null ? t.getEstado().toString() : "SIN_ESTADO",
                t.getBase() != null ? t.getBase().getNombre() : "Sin base",
                t.getBase() != null ? t.getBase().getId() : null,
                t.isActivo()
        );
    }
}