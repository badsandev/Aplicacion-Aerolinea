package com.tallerpiloto.piloto.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "base")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder


public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "codigo_iata", length = 3, unique = true)
    private String codigoIata;

    @Column(name = "codigo_icao", length = 4, unique = true)
    private String codigoIcao;

    private String ciudad;
    private String pais;

    @Column(name = "es_base_mantenimiento")
    private boolean esBaseMantenimiento = false;



}
