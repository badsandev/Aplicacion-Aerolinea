package com.tallerpiloto.piloto.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder
public class Tripulante extends Persona {

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_tripulante")
    private RolTripulante rolTripulante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual")
    private EstadoPersonalAereo estado;



    @OneToOne(mappedBy = "tripulante")
    private Usuario usuario;
}
