package com.tallerpiloto.piloto.dto;


import com.tallerpiloto.piloto.model.EstadoVuelo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VueloDTO {
    @NotBlank(message = "El número de vuelo no puede estar vacío")
    private String numVuelo;

    @NotNull(message = "El origen no puede ser nulo")
    private Long origenId;

    @NotNull(message = "El destino no puede ser nulo")
    private Long destinoId;

    @NotNull(message = "La fecha de salida no puede ser nula")
    private LocalDateTime fechaHoraSalida;

    private LocalDateTime fechaHoraLlegada;

    private EstadoVuelo estado;

    @NotNull(message = "El avión no puede ser nulo")
    private Long avionId;

    @NotNull(message = "El piloto no puede ser nulo")
    private Long pilotoId;

    private List<Long> tripulacionIds;


}
