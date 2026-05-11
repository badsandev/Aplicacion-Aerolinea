package com.tallerpiloto.piloto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "piloto")
@SuperBuilder
public class Piloto extends Persona{
    
    private String licencia;

    @Column(name = "horas_de_vuelo")
    private Double horasDeVuelo;



    @OneToOne(mappedBy = "piloto")
    private Usuario usuario;

}
