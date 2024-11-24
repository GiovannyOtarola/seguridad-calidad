package com.duoc.seguridad_calidad.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;



@Component
@Log
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

         // Obtener el rol del usuario desde las autoridades
        String role = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse("ROLE_USER"); // Si no hay rol asignado, retornar vacío (o "ROLE_USER" por defecto)


        // Redirigir a la página correspondiente según el rol
        if ("ROLE_ADMIN".equals(role)) {
            log.info("Redirigiendo a /admin para rol ADMIN");
            response.sendRedirect("/admin");  // Redirige a la página del admin
        } else {
            log.info("Redirigiendo a /home para rol USER");
            response.sendRedirect("/home");   // Redirige a la página de usuario
        }
    }
}

