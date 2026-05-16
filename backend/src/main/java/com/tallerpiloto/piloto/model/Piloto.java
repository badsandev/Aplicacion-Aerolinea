package com.tallerpiloto.piloto.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "piloto")
@SuperBuilder
public class Piloto extends Persona{
    
    private String licencia;

    @Column(name = "horas_de_vuelo")
    private Double horasDeVuelo;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual")
    private EstadoPersonalAereo estado;





    @OneToOne(mappedBy = "piloto")
    private Usuario usuario;

}
