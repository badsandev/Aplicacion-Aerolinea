package com.tallerpiloto.piloto.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "base_id")
    private Base base;

    @Builder.Default
    private boolean activo = true;

}
