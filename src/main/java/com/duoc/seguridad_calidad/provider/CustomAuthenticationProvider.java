package com.duoc.seguridad_calidad.provider;

import com.duoc.seguridad_calidad.model.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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





@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final TokenStore tokenStore;

    @Autowired
    public CustomAuthenticationProvider(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        System.out.println("Llegué a Custom Authentication Provider");

        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        System.out.println("Name: " + name);
        System.out.println("Password: " + password);

        // Crea el cuerpo de la solicitud
        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("user", name);
        requestBody.add("encryptedPass", password);

        final var restTemplate = new RestTemplate();
        try {
            // Realiza la llamada a la API de autenticación
            final var responseEntity = restTemplate.postForEntity("http://localhost:8080/login", requestBody, String.class);
            System.out.println("Response Entity: " + responseEntity);

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new BadCredentialsException("Invalid username or password");
            }

            // Guarda el token si la respuesta es exitosa
            tokenStore.setToken(responseEntity.getBody());
            System.out.println("Token Store: " + tokenStore.getToken());

            // Configura las autoridades (roles)
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            // Retorna el token de autenticación
            return new UsernamePasswordAuthenticationToken(name, password, authorities);

        } catch (Exception ex) {
            System.out.println("Error during authentication: " + ex.getMessage());
            throw new BadCredentialsException("Invalid username or password", ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}