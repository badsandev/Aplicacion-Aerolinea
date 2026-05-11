package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.Avion;
import com.tallerpiloto.piloto.model.Base;
import com.tallerpiloto.piloto.model.Piloto;

public record AvionResponseDTO(

        Long id,

    String codigo,
    String tipo,
    String base,
    Integer capacidad,
    Double horasDeVuelo,
    String estado


) {

    public static AvionResponseDTO fromEntity(Avion avion) {
        return new AvionResponseDTO(
                avion.getId(),
                avion.getCodigo(),
                avion.getTipo(),
                avion.getBase() != null ? avion.getBase().getNombre() : "Sin base",
                avion.getCapacidad(),
                avion.getHorasDeVuelo(),
                avion.getEstado().toString()

        );
    }




}
