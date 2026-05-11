package com.tallerpiloto.piloto.repository;

import com.tallerpiloto.piloto.model.Tripulante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TripulanteRepository extends JpaRepository<Tripulante, Long> {

    Optional<Tripulante> findByCodigo(String codigo);

    @Query("SELECT t FROM Tripulante t " +
            "LEFT JOIN FETCH t.base " +
            "WHERE t.activo = true")
    List<Tripulante> findAllActivosConBase();
}
