package com.tallerpiloto.piloto.service;


import com.tallerpiloto.piloto.dto.AvionDTO;
import com.tallerpiloto.piloto.dto.AvionResponseDTO;
import com.tallerpiloto.piloto.model.Avion;
import com.tallerpiloto.piloto.model.Base;
import com.tallerpiloto.piloto.model.EstadoAvion;
import com.tallerpiloto.piloto.repository.AvionRepository;
import com.tallerpiloto.piloto.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvionService {

    private final AvionRepository avionRepository;
    private final BaseRepository baseRepository;


    public AvionResponseDTO crear(AvionDTO dto) {
        if (avionRepository.findByCodigo(dto.getCodigo()).isPresent()) {
            throw new RuntimeException("Ya existe un avión con ese código");
        }

        Base base = null;
        if (dto.getBaseId() != null) {
            base = baseRepository.findById(dto.getBaseId())
                    .orElseThrow(() -> new RuntimeException("Base no encontrada"));
        }

        Avion avion = Avion.builder()
                .codigo(dto.getCodigo())
                .tipo(dto.getTipo())
                .capacidad(dto.getCapacidad())
                .horasDeVuelo(dto.getHorasDeVuelo() != null ? dto.getHorasDeVuelo() : 0.0)
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoAvion.DISPONIBLE)
                .yearFabricacion(dto.getYearFabricacion())
                .ultimoMantenimiento(dto.getUltimoMantenimiento())
                .proximoMantenimiento(dto.getUltimoMantenimiento() != null ?
                        dto.getUltimoMantenimiento().plusMonths(6) : null)
                .base(base)
                .build();

        return AvionResponseDTO.fromEntity(avionRepository.save(avion));
    }

    public List<AvionResponseDTO> listarTodos() {
        return avionRepository.findAllActivosConBase()
                .stream()
                .map(AvionResponseDTO::fromEntity)
                .toList();
    }

    public AvionResponseDTO buscarPorId(Long id) {
        return avionRepository.findById(id)
                .map(AvionResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Avión no encontrado"));
    }
    private Avion buscarEntidadPorId(Long id) {
        return avionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avión no encontrado"));
    }

    public AvionResponseDTO actualizar(Long id, AvionDTO dto) {
        Avion avion = buscarEntidadPorId(id);
        avion.setCodigo(dto.getCodigo());
        avion.setTipo(dto.getTipo());
        avion.setCapacidad(dto.getCapacidad());
        avion.setYearFabricacion(dto.getYearFabricacion());

        if (dto.getHorasDeVuelo() != null) {
            avion.setHorasDeVuelo(dto.getHorasDeVuelo());
        }
        if (dto.getEstado() != null) {
            avion.setEstado(dto.getEstado());
        }
        if (dto.getBaseId() != null) {
            Base base = baseRepository.findById(dto.getBaseId())
                    .orElseThrow(() -> new RuntimeException("Base no encontrada"));
            avion.setBase(base);
        }

        return AvionResponseDTO.fromEntity(avionRepository.save(avion));
    }


    public void eliminar(Long id) {
        Avion avion = buscarEntidadPorId(id);
        avion.setEstado(EstadoAvion.INACTIVO);
        avionRepository.save(avion);
    }
    public void reactivar(Long id){
        Avion avion = buscarEntidadPorId(id);
        avion.setEstado(EstadoAvion.DISPONIBLE);
        avionRepository.save(avion);
    }

    public AvionResponseDTO registrarMantenimiento(Long id) {
        Avion avion = buscarEntidadPorId(id);
        avion.setUltimoMantenimiento(LocalDate.now());
        avion.setProximoMantenimiento(LocalDate.now().plusMonths(6));
        avion.setEstado(EstadoAvion.OPERATIVO);
        return AvionResponseDTO.fromEntity(avionRepository.save(avion));
    }







}
