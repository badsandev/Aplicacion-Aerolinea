package com.tallerpiloto.piloto.repository;

import com.tallerpiloto.piloto.model.Avion;
import com.tallerpiloto.piloto.model.EstadoAvion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AvionRepository extends JpaRepository<Avion, Long> {

    Optional<Avion> findByCodigo(String codigo);

    List<Avion> findByEstado(EstadoAvion estado);

    @Query("SELECT a FROM Avion a LEFT JOIN FETCH a.base WHERE a.estado != 'INACTIVO'")
    List<Avion> findAllActivosConBase();

}
