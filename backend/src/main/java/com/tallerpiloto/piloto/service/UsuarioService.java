package com.tallerpiloto.piloto.service;

import com.tallerpiloto.piloto.dto.UsuarioDTO;
import com.tallerpiloto.piloto.dto.UsuarioResponseDTO;
import com.tallerpiloto.piloto.model.*;
import com.tallerpiloto.piloto.repository.BaseRepository;
import com.tallerpiloto.piloto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Usuario registar(UsuarioDTO dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Usuario ya existe");
        }
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email ya existe");
        }

        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol() != null ? dto.getRol() : Rol.OPERADOR)
                .activo(true)
                .build();

        // Configuración inicial de perfil técnico
        if (dto.getRol() == Rol.PILOTO) {
            configurarNuevoPiloto(usuario, dto);
        } else if (dto.getRol() == Rol.TRIPULANTE) {
            configurarNuevoTripulante(usuario, dto);
        }

        return usuarioRepository.save(usuario);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findByActivoTrue()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private Usuario buscarEntidadPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarEntidadPorId(id);

        // Actualización de campos básicos del Usuario
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            usuario.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRol() != null && usuario.getRol() != Rol.ADMIN) {
            usuario.setRol(dto.getRol());
        }

        if (usuario.getRol() == Rol.PILOTO && usuario.getPiloto() != null) {
            actualizarDatosPiloto(usuario.getPiloto(), dto);
        }
        else if (usuario.getRol() == Rol.TRIPULANTE && usuario.getTripulante() != null) {
            actualizarDatosTripulante(usuario.getTripulante(), dto);
        }

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }


    private void configurarNuevoPiloto(Usuario usuario, UsuarioDTO dto) {
        Base base = dto.getBaseId() != null ?
                baseRepository.findById(dto.getBaseId()).orElse(null) : null;

        usuario.setPiloto(Piloto.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .licencia(dto.getLicencia())
                .horasDeVuelo(dto.getHorasDeVuelo())
                .base(base)
                .build());
    }

    private void configurarNuevoTripulante(Usuario usuario, UsuarioDTO dto) {
        Base base = dto.getBaseId() != null ?
                baseRepository.findById(dto.getBaseId()).orElse(null) : null;

        usuario.setTripulante(Tripulante.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .rolTripulante(dto.getRolTripulante())
                .base(base)
                .build());
    }

    private void actualizarDatosPiloto(Piloto piloto, UsuarioDTO dto) {
        piloto.setNombre(dto.getNombre());
        piloto.setCodigo(dto.getCodigo());
        piloto.setLicencia(dto.getLicencia());
        piloto.setHorasDeVuelo(dto.getHorasDeVuelo());
        if (dto.getBaseId() != null) {
            piloto.setBase(baseRepository.findById(dto.getBaseId()).orElse(null));
        }
    }

    private void actualizarDatosTripulante(Tripulante tripulante, UsuarioDTO dto) {
        tripulante.setNombre(dto.getNombre());
        tripulante.setCodigo(dto.getCodigo());
        tripulante.setRolTripulante(dto.getRolTripulante());
        if (dto.getBaseId() != null) {
            tripulante.setBase(baseRepository.findById(dto.getBaseId()).orElse(null));
        }
    }

    // --- GESTIÓN DE ESTADO Y SEGURIDAD ---

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscarEntidadPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void reactivar(Long id) {
        Usuario usuario = buscarEntidadPorId(id);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public String generarTokenRecuperacion(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpiracion(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);
        return token;
    }

    @Transactional
    public void cambiarPassword(String token, String newpassword) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacion(token)
                .orElseThrow(() -> new RuntimeException("Token no valido"));

        if (usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token ya esta expirado");
        }

        usuario.setPassword(bCryptPasswordEncoder.encode(newpassword));
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void solicitarRecuperar(String email) {
        String token = generarTokenRecuperacion(email);
        emailService.enviarEmailRecuperacion(email, token);
    }
}