package com.tallerpiloto.piloto.dto;

import com.tallerpiloto.piloto.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String username,
        String email,
        String rol,
        String nombre,
        String codigo,
        String base,
        boolean activo
) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        String nombre = null;
        String codigo = null;
        String base = null;

        if (usuario.getPiloto() != null) {
            nombre = usuario.getPiloto().getNombre();
            codigo = usuario.getPiloto().getCodigo();
            base = usuario.getPiloto().getBase() != null ?
                    usuario.getPiloto().getBase().getNombre() : "Sin base";
        } else if (usuario.getTripulante() != null) {
            nombre = usuario.getTripulante().getNombre();
            codigo = usuario.getTripulante().getCodigo();
            base = usuario.getTripulante().getBase() != null ?
                    usuario.getTripulante().getBase().getNombre() : "Sin base";
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol().name(),
                nombre,
                codigo,
                base,
                usuario.isActivo()
        );
    }
}
