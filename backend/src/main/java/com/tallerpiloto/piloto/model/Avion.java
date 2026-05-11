package com.tallerpiloto.piloto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "avion")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder

public class Avion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;
    @Column(name = "tipo_de_avion")
    private String tipo;

    private Integer capacidad;

    @Column(name = "horas_de_vuelo")
    private Double horasDeVuelo;

    @Enumerated(EnumType.STRING)
    private EstadoAvion estado;


    @ManyToOne
    @JoinColumn(name = "base_id")
    private Base base;

    @Column(name = "year_fabricacion")
    private Integer yearFabricacion;
    @Column(name = "ultimo_mantenimiento")
    private LocalDate ultimoMantenimiento;

    @Column(name = "proximo_mantenimiento")
    private LocalDate proximoMantenimiento;


}
