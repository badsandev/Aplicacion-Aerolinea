package com.tallerpiloto.piloto.dto;
import com.tallerpiloto.piloto.model.EstadoPersonalAereo;
import com.tallerpiloto.piloto.model.Piloto;
public record PilotoResponseDTO(

        Long id,
        String codigo,
        String nombre,
        String licencia,
        Double horasDeVuelo,
        String base,
        Long baseId,
        EstadoPersonalAereo estado,
        boolean activo

) {

    public static PilotoResponseDTO fromEntity(Piloto piloto) {

        return new PilotoResponseDTO(

                piloto.getId(),
                piloto.getCodigo(),
                piloto.getNombre(),
                piloto.getLicencia(),
                piloto.getHorasDeVuelo(),

                piloto.getBase() != null
                        ? piloto.getBase().getNombre()
                        : "Sin base",

                piloto.getBase() != null
                        ? piloto.getBase().getId()
                        : null,

                piloto.getEstado() != null
                        ? piloto.getEstado()
                        : EstadoPersonalAereo.DISPONIBLE,

                piloto.isActivo()
        );
    }
}