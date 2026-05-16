package com.tallerpiloto.piloto.service;

import com.tallerpiloto.piloto.dto.VueloDTO;
import com.tallerpiloto.piloto.dto.VueloResponseDTO;
import com.tallerpiloto.piloto.model.*;
import com.tallerpiloto.piloto.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VueloService {

    private final VueloRepository vueloRepository;
    private final AvionRepository avionRepository;
    private final PilotoRepository pilotoRepository;
    private final TripulanteRepository tripulanteRepository;
    private final BaseRepository baseRepository;

    public Vuelo crear(VueloDTO dto) {
        if (vueloRepository.findByNumVuelo(dto.getNumVuelo()).isPresent()) {
            throw new RuntimeException("Ya existe un vuelo con ese numero");
        }

        Base origen = baseRepository.findById(dto.getOrigenId())
                .orElseThrow(() -> new RuntimeException("Base origen no encontrada"));
        Base destino = baseRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Base destino no encontrada"));
        Avion avion = avionRepository.findById(dto.getAvionId())
                .orElseThrow(() -> new RuntimeException("Avion no encontrado"));
        Piloto piloto = pilotoRepository.findById(dto.getPilotoId())
                .orElseThrow(() -> new RuntimeException("Piloto no encontrado"));

        List<Tripulante> tripulacion = new ArrayList<>();
        if (dto.getTripulacionIds() != null) {
            tripulacion = dto.getTripulacionIds().stream()
                    .map(id -> tripulanteRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Tripulante no encontrado: " + id)))
                    .collect(Collectors.toList());
        }

        Vuelo vuelo = Vuelo.builder()
                .numVuelo(dto.getNumVuelo())
                .origen(origen)
                .destino(destino)
                .fechaHoraSalida(dto.getFechaHoraSalida())
                .fechaHoraLlegada(dto.getFechaHoraLlegada())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoVuelo.PROGRAMADO)
                .avion(avion)
                .piloto(piloto)
                .tripulacion(tripulacion)
                .build();

        avion.setEstado(EstadoAvion.EN_VUELO);
        piloto.setEstado(EstadoPersonalAereo.EN_VUELO);
        tripulacion.forEach(t -> t.setEstado(EstadoPersonalAereo.EN_VUELO));

        avionRepository.save(avion);
        pilotoRepository.save(piloto);
        tripulanteRepository.saveAll(tripulacion);

        return vueloRepository.save(vuelo);
    }

    public VueloResponseDTO buscarPorId(Long id) {
        return vueloRepository.findById(id)
                .map(VueloResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));
    }

    private Vuelo buscarPorEntidad(Long id) {
        return vueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));
    }

    public List<VueloResponseDTO> listarTodos() {
        return vueloRepository.findAllConDetalles()
                .stream()
                .map(VueloResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public VueloResponseDTO actualizar(Long id, VueloDTO dto) {
        Vuelo vuelo = buscarPorEntidad(id);

        if (vuelo.getPiloto() != null) {
            vuelo.getPiloto().setEstado(EstadoPersonalAereo.DISPONIBLE);
            pilotoRepository.save(vuelo.getPiloto());
        }
        vuelo.getTripulacion().forEach(t -> {
            t.setEstado(EstadoPersonalAereo.DISPONIBLE);
            tripulanteRepository.save(t);
        });

        vuelo.setNumVuelo(dto.getNumVuelo());

        if (dto.getOrigenId() != null) {
            Base origen = baseRepository.findById(dto.getOrigenId())
                    .orElseThrow(() -> new RuntimeException("Base origen no encontrada"));
            vuelo.setOrigen(origen);
        }
        if (dto.getDestinoId() != null) {
            Base destino = baseRepository.findById(dto.getDestinoId())
                    .orElseThrow(() -> new RuntimeException("Base destino no encontrada"));
            vuelo.setDestino(destino);
        }

        vuelo.setFechaHoraSalida(dto.getFechaHoraSalida());
        vuelo.setFechaHoraLlegada(dto.getFechaHoraLlegada());

        if (dto.getEstado() != null) {
            vuelo.setEstado(dto.getEstado());
        }
        if (dto.getAvionId() != null) {
            Avion avion = avionRepository.findById(dto.getAvionId())
                    .orElseThrow(() -> new RuntimeException("Avion no encontrado"));
            vuelo.setAvion(avion);
            avion.setEstado(EstadoAvion.EN_VUELO);
            avionRepository.save(avion);
        }
        if (dto.getPilotoId() != null) {
            Piloto piloto = pilotoRepository.findById(dto.getPilotoId())
                    .orElseThrow(() -> new RuntimeException("Piloto no encontrado"));
            vuelo.setPiloto(piloto);
            piloto.setEstado(EstadoPersonalAereo.EN_VUELO);
            pilotoRepository.save(piloto);
        }
        if (dto.getTripulacionIds() != null) {
            List<Tripulante> tripulacion = dto.getTripulacionIds().stream()
                    .map(tripId -> tripulanteRepository.findById(tripId)
                            .orElseThrow(() -> new RuntimeException("Tripulante no encontrado")))
                    .collect(Collectors.toList());
            tripulacion.forEach(t -> {
                t.setEstado(EstadoPersonalAereo.EN_VUELO);
                tripulanteRepository.save(t);
            });
            vuelo.setTripulacion(tripulacion);
        }

        return VueloResponseDTO.fromEntity(vueloRepository.save(vuelo));
    }

    public void cancelar(Long id) {
        Vuelo vuelo = buscarPorEntidad(id);
        vuelo.setEstado(EstadoVuelo.CANCELADO);

        if (vuelo.getAvion() != null) {
            vuelo.getAvion().setEstado(EstadoAvion.DISPONIBLE);
            avionRepository.save(vuelo.getAvion());
        }
        if (vuelo.getPiloto() != null) {
            vuelo.getPiloto().setEstado(EstadoPersonalAereo.DISPONIBLE);
            pilotoRepository.save(vuelo.getPiloto());
        }
        vuelo.getTripulacion().forEach(t -> {
            t.setEstado(EstadoPersonalAereo.DISPONIBLE);
            tripulanteRepository.save(t);
        });

        vueloRepository.save(vuelo);
    }

    public void retrasar(Long id, LocalDateTime nuevaFechaSalida) {
        Vuelo vuelo = buscarPorEntidad(id);
        if (vuelo.getEstado() != EstadoVuelo.PROGRAMADO) {
            throw new RuntimeException("Solo se pueden retrasar vuelos programados");
        }
        vuelo.setEstado(EstadoVuelo.RETRASADO);
        vuelo.setFechaHoraSalida(nuevaFechaSalida);
        vueloRepository.save(vuelo);
    }

    public List<VueloResponseDTO> listarPorPiloto(Long pilotoId) {
        return vueloRepository.findByPilotoId(pilotoId)
                .stream()
                .map(VueloResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<VueloResponseDTO> listarPorTripulante(Long tripulanteId) {
        return vueloRepository.findByTripulanteId(tripulanteId)
                .stream()
                .map(VueloResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<VueloResponseDTO> listarPorEstado(EstadoVuelo estado) {
        return vueloRepository.findByEstado(estado)
                .stream()
                .map(VueloResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Vuelo aterrizar(Long id) {
        Vuelo vuelo = buscarPorEntidad(id);
        vuelo.setEstado(EstadoVuelo.ATERRIZADO);
        vuelo.setFechaHoraLlegada(LocalDateTime.now());

        if (vuelo.getFechaHoraSalida() != null) {
            double horasVuelo = Duration.between(
                    vuelo.getFechaHoraSalida(),
                    LocalDateTime.now()
            ).toMinutes() / 60.0;

            Piloto piloto = vuelo.getPiloto();
            piloto.setHorasDeVuelo(piloto.getHorasDeVuelo() + horasVuelo);
            piloto.setEstado(EstadoPersonalAereo.DISPONIBLE);
            pilotoRepository.save(piloto);

            Avion avion = vuelo.getAvion();
            avion.setHorasDeVuelo(avion.getHorasDeVuelo() + horasVuelo);
            avion.setEstado(EstadoAvion.DISPONIBLE);
            avionRepository.save(avion);

            // ✅ liberar tripulantes
            vuelo.getTripulacion().forEach(t -> {
                t.setEstado(EstadoPersonalAereo.DISPONIBLE);
                tripulanteRepository.save(t);
            });
        }

        return vueloRepository.save(vuelo);
    }
}