package com.tallerpiloto.piloto.service;


import com.tallerpiloto.piloto.dto.PilotoDTO;
import com.tallerpiloto.piloto.dto.PilotoResponseDTO;
import com.tallerpiloto.piloto.model.Base;
import com.tallerpiloto.piloto.model.Piloto;
import com.tallerpiloto.piloto.repository.BaseRepository;
import com.tallerpiloto.piloto.repository.PilotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PilotoService {

    private final PilotoRepository pilotoRepository;
    private final BaseRepository baseRepository;


    public Piloto crearPiloto(PilotoDTO dto){

        if(pilotoRepository.findByCodigo(dto.getCodigo()).isPresent()){
            throw new RuntimeException("Ya existe un piloto con ese código");
        }

        Piloto piloto = Piloto.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .licencia(dto.getLicencia())
                .horasDeVuelo(dto.getHorasDeVuelo())

                .build();

        return pilotoRepository.save(piloto);


    }

    public List<PilotoResponseDTO> listarTodos() {
        return pilotoRepository.findAllActivosConBase()
                .stream()
                .map(PilotoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PilotoResponseDTO buscarPilotoPorId(Long id) {
        return pilotoRepository.findById(id)
                .map(PilotoResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Piloto no encontrado"));
    }

    private Piloto buscarEntidadPorId(Long id) {
        return pilotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piloto no encontrado"));
    }

    public PilotoResponseDTO actualizarPiloto(Long id, PilotoDTO dto) {
        Piloto piloto = buscarEntidadPorId(id);

        piloto.setCodigo(dto.getCodigo());
        piloto.setNombre(dto.getNombre());
        piloto.setLicencia(dto.getLicencia());
        piloto.setHorasDeVuelo(dto.getHorasDeVuelo());

        if (dto.getBaseId() != null) {
            Base base = baseRepository.findById(dto.getBaseId())
                    .orElseThrow(() -> new RuntimeException("Base no encontrada"));
            piloto.setBase(base);
        }

        return PilotoResponseDTO.fromEntity(pilotoRepository.save(piloto));
    }

    public void eliminar(Long id){
        Piloto piloto = buscarEntidadPorId(id);
        piloto.setActivo(false);
        pilotoRepository.save(piloto);
    }
    public void reactivar(Long id){
        Piloto piloto = buscarEntidadPorId(id);
        piloto.setActivo(true);
        pilotoRepository.save(piloto);
    }




}
