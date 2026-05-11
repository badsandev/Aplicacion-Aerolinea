package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.EstadoAvion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AvionDTO {

    @NotBlank(message = "El código no puede estar vacío")
    private String codigo;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    @NotNull(message = "La capacidad no puede ser nula")
    @Positive(message = "La capacidad debe ser positiva")
    private Integer capacidad;

    private Double horasDeVuelo;
    private EstadoAvion estado;
    private Integer yearFabricacion;
    private LocalDate ultimoMantenimiento;
    private Long baseId;
}