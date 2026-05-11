package com.tallerpiloto.piloto.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PilotoDTO {


    @NotBlank(message = "El codigo no puede estar vacio")
    private String codigo;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "La licencia no puede estar vacia")
    private String licencia;

    @NotNull(message = "Las horas de vuelo no pueden ser nulas")
    @Positive(message = "Las horas de vuelo deben ser un numero positivo")
    private Double horasDeVuelo;

    private Long baseId;

}
