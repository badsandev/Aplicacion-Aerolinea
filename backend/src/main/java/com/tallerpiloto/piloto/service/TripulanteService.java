package com.tallerpiloto.piloto.service;

import com.tallerpiloto.piloto.dto.TripulanteDTO;
import com.tallerpiloto.piloto.dto.TripulanteResponseDTO;
import com.tallerpiloto.piloto.model.Base;
import com.tallerpiloto.piloto.model.EstadoPersonalAereo;
import com.tallerpiloto.piloto.model.Tripulante;
import com.tallerpiloto.piloto.repository.BaseRepository;
import com.tallerpiloto.piloto.repository.TripulanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripulanteService {

    private final TripulanteRepository tripulanteRepository;
    private final BaseRepository baseRepository;

    public TripulanteResponseDTO crear(TripulanteDTO dto) {
        if (tripulanteRepository.findByCodigo(dto.getCodigo()).isPresent()) {
            throw new RuntimeException("Ya existe un tripulante con ese código");
        }
        Base base = null;
        if (dto.getBaseId() != null) {
            base = baseRepository.findById(dto.getBaseId())
                    .orElseThrow(() -> new RuntimeException("Base no encontrada"));
        }

        Tripulante tripulante = Tripulante.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .rolTripulante(dto.getRol())
                .estado(dto.getEstado() != null
                        ? dto.getEstado()
                        : EstadoPersonalAereo.DISPONIBLE)
                .base(base)
                .activo(true)
                .build();

        return TripulanteResponseDTO.fromEntity(tripulanteRepository.save(tripulante));
    }

    public List<TripulanteResponseDTO> listarTodos() {
        return tripulanteRepository.findAllActivosConBase()
                .stream()
                .map(TripulanteResponseDTO::fromEntity)
                .toList();
    }

    public TripulanteResponseDTO buscarPorId(Long id) {
        Tripulante tripulante = tripulanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tripulante no encontrado"));
        return TripulanteResponseDTO.fromEntity(tripulante);
    }

    public TripulanteResponseDTO actualizar(Long id, TripulanteDTO dto) {

        Tripulante tripulante = tripulanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tripulante no encontrado"));

        tripulante.setCodigo(dto.getCodigo());
        tripulante.setNombre(dto.getNombre());
        tripulante.setRolTripulante(dto.getRol());

        if (dto.getEstado() != null) {
            tripulante.setEstado(dto.getEstado());
        }

        if (dto.getEstado() == EstadoPersonalAereo.EN_VUELO) {
            throw new RuntimeException("No puedes asignar manualmente EN_VUELO");
        }

        if (dto.getBaseId() != null) {
            Base base = baseRepository.findById(dto.getBaseId())
                    .orElseThrow(() -> new RuntimeException("Base no encontrada"));
            tripulante.setBase(base);
        }

        return TripulanteResponseDTO.fromEntity(
                tripulanteRepository.save(tripulante)
        );
    }

    public void eliminar(Long id) {
        Tripulante tripulante = tripulanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tripulante no encontrado"));
        tripulante.setActivo(false);
        tripulanteRepository.save(tripulante);
    }

    public void reactivar(Long id) {
        Tripulante tripulante = tripulanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tripulante no encontrado"));
        tripulante.setActivo(true);
        tripulanteRepository.save(tripulante);
    }
}
