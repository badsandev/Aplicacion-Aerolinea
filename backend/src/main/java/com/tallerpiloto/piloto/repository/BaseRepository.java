package com.tallerpiloto.piloto.repository;

import com.tallerpiloto.piloto.model.Base;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseRepository extends JpaRepository<Base, Long> {

    Optional<Base> findByNombre(String nombre);
    Optional<Base> findByCodigoIata(String codigoIata);


}
