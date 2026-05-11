package com.tallerpiloto.piloto.repository;

import com.tallerpiloto.piloto.model.Vuelo;
import com.tallerpiloto.piloto.model.EstadoVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {

    Optional<Vuelo> findByNumVuelo(String numVuelo);

    @Query("SELECT v FROM Vuelo v " +
            "LEFT JOIN FETCH v.avion " +
            "LEFT JOIN FETCH v.piloto " +
            "LEFT JOIN FETCH v.origen " +
            "LEFT JOIN FETCH v.destino " +
            "LEFT JOIN FETCH v.tripulacion")
    List<Vuelo> findAllConDetalles();

    @Query("SELECT v FROM Vuelo v " +
            "LEFT JOIN FETCH v.avion " +
            "LEFT JOIN FETCH v.piloto " +
            "LEFT JOIN FETCH v.origen " +
            "LEFT JOIN FETCH v.destino " +
            "LEFT JOIN FETCH v.tripulacion " +
            "WHERE v.piloto.id = :pilotoId")
    List<Vuelo> findByPilotoId(@Param("pilotoId") Long pilotoId);

    @Query("SELECT v FROM Vuelo v " +
            "JOIN v.tripulacion t " +
            "LEFT JOIN FETCH v.avion " +
            "LEFT JOIN FETCH v.piloto " +
            "LEFT JOIN FETCH v.origen " +
            "LEFT JOIN FETCH v.destino " +
            "WHERE t.id = :tripulanteId")
    List<Vuelo> findByTripulanteId(@Param("tripulanteId") Long tripulanteId);

    @Query("SELECT v FROM Vuelo v " +
            "LEFT JOIN FETCH v.avion " +
            "LEFT JOIN FETCH v.piloto " +
            "LEFT JOIN FETCH v.origen " +
            "LEFT JOIN FETCH v.destino " +
            "WHERE v.estado = :estado")
    List<Vuelo> findByEstado(@Param("estado") EstadoVuelo estado);

    @Query("SELECT v FROM Vuelo v "+
    "LEFT JOIN FETCH v.avion " +
    "LEFT JOIN FETCH v.piloto " +
    "LEFT JOIN FETCH v.origen " +
    "LEFT JOIN FETCH v.destino " +
    "LEFT JOIN FETCH v.tripulacion " +
    "WHERE v.fechaHoraSalida BETWEEN :inicio AND :fin")
    List<Vuelo> findVuelosProgramadosHoy(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);






}
