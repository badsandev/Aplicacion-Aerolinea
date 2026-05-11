package com.tallerpiloto.piloto.service;

import com.tallerpiloto.piloto.dto.BaseDTO;
import com.tallerpiloto.piloto.model.Base;
import com.tallerpiloto.piloto.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseService {
    private final BaseRepository baseRepository;


    public Base crear(BaseDTO dto) {
        Base base = Base.builder()
                .nombre(dto.getNombre())
                .codigoIata(dto.getCodigoIata())
                .codigoIcao(dto.getCodigoIcao())
                .ciudad(dto.getCiudad())
                .pais(dto.getPais())
                .esBaseMantenimiento(dto.isEsBaseMantenimiento())
                .build();
        return baseRepository.save(base);
    }
    public List<Base> listarTodos() {
        return baseRepository.findAll();
    }

    public Base buscarPorId(Long id) {
        return baseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Base no encontrada"));
    }

    public Base actualizar(Long id, BaseDTO dto) {
        Base base = buscarPorId(id);

        if (dto.getNombre() != null) base.setNombre(dto.getNombre());

        // solo actualiza codigoIata si es diferente al actual
        if (dto.getCodigoIata() != null &&
                !dto.getCodigoIata().equals(base.getCodigoIata())) {
            baseRepository.findByCodigoIata(dto.getCodigoIata())
                    .ifPresent(b -> { throw new RuntimeException("Código IATA ya existe"); });
            base.setCodigoIata(dto.getCodigoIata());
        }

        // solo actualiza codigoIcao si es diferente al actual
        if (dto.getCodigoIcao() != null &&
                !dto.getCodigoIcao().equals(base.getCodigoIcao())) {
            baseRepository.findByCodigoIata(dto.getCodigoIcao())
                    .ifPresent(b -> { throw new RuntimeException("Código ICAO ya existe"); });
            base.setCodigoIcao(dto.getCodigoIcao());
        }

        if (dto.getCiudad() != null) base.setCiudad(dto.getCiudad());
        if (dto.getPais() != null) base.setPais(dto.getPais());
        base.setEsBaseMantenimiento(dto.isEsBaseMantenimiento());

        return baseRepository.save(base);
    }



    public void eliminar(Long id){

        Base base = buscarPorId(id);
        base.setEsBaseMantenimiento(true);
        baseRepository.save(base);

    }
    public void reactivar(Long id){
        Base base = buscarPorId(id);
        base.setEsBaseMantenimiento(false);
        baseRepository.save(base);
    }




}
