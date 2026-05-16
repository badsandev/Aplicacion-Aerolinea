package com.tallerpiloto.piloto.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private boolean activo = true;

    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "token_expiracion")
    private LocalDateTime tokenExpiracion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "piloto_id")
    private Piloto piloto;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tripulante_id")
    private Tripulante tripulante;

}
