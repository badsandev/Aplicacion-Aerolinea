package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.EstadoPersonalAereo;
import com.tallerpiloto.piloto.model.RolTripulante;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TripulanteDTO {

    @NotBlank(message = "El código no puede estar vacío")
    private String codigo;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    private EstadoPersonalAereo estado;


    private RolTripulante rol;

    private Long baseId;
}
