package com.duoc.seguridad_calidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.client.RestTemplate;

import com.duoc.seguridad_calidad.model.TokenStore;
import com.duoc.seguridad_calidad.provider.CustomAuthenticationProvider;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CustomAuthenticationProviderTest {
    
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TokenStore tokenStore;

    @InjectMocks
    private CustomAuthenticationProvider customAuthenticationProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada test
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testAuthenticateToken() {
         
        // Simular la respuesta de la API con un token de autenticación
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGlsZXMiOlsiUk9MRV9VU0VSIl0sInN1YiI6InVzZXIxIiwiYWFyIjoxMjM0NTY3ODkwMCwiZXhwIjoxNzMyNTYyOTc0fQ.Fvp6nWZFyptU4b0QLYbsKCFFCt6Ard-1V3rb7Sn790SwREFlHaq3TvmqnHVesm3nkMoRvus46bNc0S06b7on2g";

        // Simular una respuesta de RestTemplate
        ResponseEntity<String> responseEntity = ResponseEntity.ok(token);
        when(restTemplate.exchange(eq("http://localhost:8080/login"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);
        equals(token); // Verificar que el token es el esperado
    }
    

    @Test
    void testAuthenticateFailure() {
        // Simular un error en la autenticación
        String username = "user";
        String password = "wrongPassword";

        // Simulamos una respuesta de error (por ejemplo, una respuesta HTTP 403)
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FORBIDDEN);

        // Configuramos el mock de RestTemplate para que devuelva una respuesta de error
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
            .thenReturn(response);

        // Crear un objeto de autenticación (simulando el usuario que se autentica)
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        // Act: Intentar autenticar (esto debe lanzar una excepción)
        try {
            customAuthenticationProvider.authenticate(authentication);
            // Si no se lanza una excepción, la prueba falla
            assert false;
        } catch (BadCredentialsException ex) {
            // Assert: Verificar que la excepción es la esperada
            assert ex.getMessage().contains("Invalid username or password");
        }
    }

    @Test
    void testSupports() {
        // Verificar que solo soporta UsernamePasswordAuthenticationToken
        assertTrue(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(customAuthenticationProvider.supports(Object.class));
    }
}
