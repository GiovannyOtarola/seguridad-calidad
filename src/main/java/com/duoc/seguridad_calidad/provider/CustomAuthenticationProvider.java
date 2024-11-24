package com.duoc.seguridad_calidad.provider;

import com.duoc.seguridad_calidad.model.AuthResponse;
import com.duoc.seguridad_calidad.model.TokenStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final TokenStore tokenStore;

    public CustomAuthenticationProvider(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    
     @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        logger.info("Llegué a Custom Authentication Provider");

        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        logger.info("Name: {}", name);
        logger.info("Password: {}", password);

        // Crea el cuerpo de la solicitud
        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("user", name);
        requestBody.add("encryptedPass", password);

        final var restTemplate = new RestTemplate();
        try {
            // Realiza la llamada a la API de autenticación
            final ResponseEntity<AuthResponse> responseEntity = restTemplate.postForEntity("http://localhost:8080/login", requestBody, AuthResponse.class);
            logger.info("Response Entity: {}", responseEntity);
            
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new BadCredentialsException("Invalid username or password");
            }

            // Obtiene el cuerpo de la respuesta
            AuthResponse authResponse = responseEntity.getBody();
            if (authResponse == null || authResponse.getToken() == null) {
                throw new BadCredentialsException("Invalid username or password");
            }

            // Guarda el token si la respuesta es exitosa
            tokenStore.setToken(authResponse.getToken());
            logger.info("Token Store: {}", tokenStore.getToken());

            // Configura las autoridades (roles)
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            String role = authResponse.getRole() != null ? authResponse.getRole() : "ROLE_USER"; // Default role
            authorities.add(new SimpleGrantedAuthority(role));

            // Retorna el token de autenticación
            return new UsernamePasswordAuthenticationToken(name, password, authorities);

        } catch (Exception ex) {
            throw new BadCredentialsException(
                "Invalid username or password for user: " + authentication.getName(), ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}