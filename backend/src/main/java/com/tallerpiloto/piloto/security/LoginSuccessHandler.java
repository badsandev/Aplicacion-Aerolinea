package com.tallerpiloto.piloto.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {

        String rol = authentication.getAuthorities()
                .iterator().next().getAuthority();

        switch (rol) {
            case "ROLE_ADMIN"      -> response.sendRedirect("/admin/dashboard");
            case "ROLE_PILOTO"     -> response.sendRedirect("/piloto/dashboard");
            case "ROLE_TRIPULANTE" -> response.sendRedirect("/tripulante/dashboard");
            default                -> response.sendRedirect("/dashboard");
        }
    }



}
