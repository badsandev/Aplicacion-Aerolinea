package com.tallerpiloto.piloto.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime; // Cambiado de LocalDate
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="vuelo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_vuelo", unique = true, nullable = false)
    private String numVuelo;

    @ManyToOne
    @JoinColumn(name = "origen_id")
    private Base origen;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    private Base destino;

    @Column(name = "fecha_hora_salida", nullable = false)
    private LocalDateTime fechaHoraSalida;

    @Column(name = "fecha_hora_llegada")
    private LocalDateTime fechaHoraLlegada;

    @Enumerated(EnumType.STRING)
    private EstadoVuelo estado;

    @ManyToOne
    @JoinColumn(name="avion_id")
    private Avion avion;

    @ManyToOne
    @JoinColumn(name="piloto_id")
    private Piloto piloto;

    @ManyToMany
    @JoinTable(
            name = "vuelo_tripulacion",
            joinColumns = @JoinColumn(name = "vuelo_id"),
            inverseJoinColumns = @JoinColumn(name = "tripulante_id")
    )
    private List<Tripulante> tripulacion = new ArrayList<>();
}