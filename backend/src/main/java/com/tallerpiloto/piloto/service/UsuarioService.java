package com.tallerpiloto.piloto.service;

import com.tallerpiloto.piloto.dto.UsuarioDTO;
import com.tallerpiloto.piloto.model.*;
import com.tallerpiloto.piloto.repository.BaseRepository;
import com.tallerpiloto.piloto.repository.UsuarioRepository;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BaseRepository baseRepository;
    private final EmailService emailService;


    public Usuario registar(UsuarioDTO dto) {

        Base base = null;

        if(usuarioRepository.findByUsername(dto.getUsername()).isPresent()){
            throw new RuntimeException("Usuario ya existe");
        }

        if(usuarioRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email ya existe");
        }

        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol() != null ? dto.getRol() : Rol.OPERADOR)
                .activo(true)
                .build();

        if (dto.getRol() == Rol.PILOTO) {



            if(dto.getBaseId() != null){
                base = baseRepository.findById(dto.getBaseId())
                        .orElseThrow(() -> new RuntimeException("Base no encontrada"));
            }

            Piloto piloto = Piloto.builder()
                    .nombre(dto.getNombre())
                    .codigo(dto.getCodigo())
                    .licencia(dto.getLicencia())
                    .horasDeVuelo(dto.getHorasDeVuelo())
                    .base(base)
                    .build();

            usuario.setPiloto(piloto);

        } else if (dto.getRol() == Rol.TRIPULANTE) {



            if(dto.getBaseId() != null){
                base = baseRepository.findById(dto.getBaseId())
                        .orElseThrow(() -> new RuntimeException("Base no encontrada"));
            }

            Tripulante tripulante = Tripulante.builder()
                    .nombre(dto.getNombre())
                    .codigo(dto.getCodigo())
                    .rolTripulante(dto.getRolTripulante())
                    .base(base)
                    .build();

            usuario.setTripulante(tripulante);
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findByActivoTrue();
    }
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    public Usuario buscarPorUsername(String username){
        return usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    public Usuario actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);

        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol());
        }

        if (dto.getRol() == Rol.PILOTO && usuario.getPiloto() != null) {
            Piloto piloto = usuario.getPiloto();
            piloto.setNombre(dto.getNombre());
            piloto.setCodigo(dto.getCodigo());
            piloto.setLicencia(dto.getLicencia());
            piloto.setHorasDeVuelo(dto.getHorasDeVuelo());
            if (dto.getBaseId() != null) {
                piloto.setBase(baseRepository.findById(dto.getBaseId()).orElse(null));
            }
        } else if (dto.getRol() == Rol.TRIPULANTE && usuario.getTripulante() != null) {
            Tripulante tripulante = usuario.getTripulante();
            tripulante.setNombre(dto.getNombre());
            tripulante.setCodigo(dto.getCodigo());
            tripulante.setRolTripulante(dto.getRolTripulante());

            if (dto.getBaseId() != null) {
                tripulante.setBase(baseRepository.findById(dto.getBaseId()).orElse(null));
            }
        }

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id){
        Usuario usuario  = buscarPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public void reactivar(Long id){
        Usuario usuario  = buscarPorId(id);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    public String generarTokenRecuperacion(String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpiracion(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        return token;
    }

    public void cambiarPassword(String token, String newpassword){
        Usuario usuario = usuarioRepository.findByTokenRecuperacion(token).
                orElseThrow(() -> new RuntimeException("Token no valido"));

        if(usuario.getTokenExpiracion().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token ya esta expirado");
        }

        usuario.setPassword(bCryptPasswordEncoder.encode(newpassword));
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);
        usuarioRepository.save(usuario);
    }

    public void solicitarRecuperar(String email){

        String token = generarTokenRecuperacion(email);
        emailService.enviarEmailRecuperacion(email, token);

    }





}
