package com.tallerpiloto.piloto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BaseDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Size(min = 3, max = 3, message = "El código IATA debe tener 3 caracteres")
    private String codigoIata;

    @Size(min = 4, max = 4, message = "El código ICAO debe tener 4 caracteres")
    private String codigoIcao;

    private String ciudad;
    private String pais;
    private boolean esBaseMantenimiento;
}
