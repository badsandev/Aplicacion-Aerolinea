package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.Rol;
import com.tallerpiloto.piloto.model.RolTripulante;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class UsuarioDTO {

    @NotBlank
    private String username;

    @Email(message = "Email no valido")
    @NotBlank(message = "El email no puede estar vacio")
    private String email;

    @Size(min = 6, message ="Minimo 6 caracteres")
    private String password;

    private Rol rol;

    private String nombre;
    private String codigo;
    private Long baseId;

    private String licencia;
    private Double horasDeVuelo;

    private RolTripulante rolTripulante;

}
