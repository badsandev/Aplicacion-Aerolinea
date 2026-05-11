package com.tallerpiloto.piloto.repository;

import com.tallerpiloto.piloto.model.Piloto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PilotoRepository extends JpaRepository<Piloto, Long> {


    @Query("SELECT p FROM Piloto p " +
            "LEFT JOIN FETCH p.base " +
            "WHERE p.activo = true")
    Optional<Piloto> findByCodigo(String codigo);


    List<Piloto> findByActivoTrue();
    @Query("SELECT p FROM Piloto p " +
            "LEFT JOIN FETCH p.base " +
            "WHERE p.activo = true")
    List<Piloto> findAllActivosConBase();







}
