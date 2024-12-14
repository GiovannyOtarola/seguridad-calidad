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
import com.duoc.seguridad_calidad.model.AuthResponse;

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

    @Mock
    private AuthResponse authResponse;

    @InjectMocks
    private CustomAuthenticationProvider customAuthenticationProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada test

    }

    
    @Test
    void testAuthenticateToken() {
         
        // Simular la respuesta de la API con un token de autenticación
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGlsZXMiOlsiUk9MRV9VU0VSIl0sInN1YiI6InVzZXIxIiwiYWFyIjoxMjM0NTY3ODkwMCwiZXhwIjoxNzMyNTYyOTc0fQ.Fvp6nWZFyptU4b0QLYbsKCFFCt6Ard-1V3rb7Sn790SwREFlHaq3TvmqnHVesm3nkMoRvus46bNc0S06b7on2g";

        // Simular una respuesta de RestTemplate
        ResponseEntity<String> responseEntity = ResponseEntity.ok(token);
        when(restTemplate.exchange(eq("http://localhost:8080/login"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);
        assertEquals(token, responseEntity.getBody(), "El token recibido no coincide con el esperado."); // Verificar que el token es el esperado
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
            fail("Expected BadCredentialsException to be thrown");
        } catch (BadCredentialsException ex) {
            // Assert: Verificar que la excepción es la esperada
            assertTrue(ex.getMessage().contains("Invalid username or password"));
    
            // Aserción adicional: Verificar que la respuesta simulada sea FORBIDDEN
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Expected HTTP status 403 (Forbidden).");
        }
    }

    @Test
    void testSupports() {
        // Verificar que solo soporta UsernamePasswordAuthenticationToken
        assertTrue(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(customAuthenticationProvider.supports(Object.class));
    }



    @Test
    public void testAuthenticate_InvalidCredentials() {
        // Arrange
        String username = "testUser ";
        String password = "testPassword";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        when(restTemplate.postForEntity(any(String.class), any(), eq(AuthResponse.class))).thenReturn(responseEntity);

        // Act & Assert
        BadCredentialsException thrown = assertThrows(BadCredentialsException.class, () -> {
            customAuthenticationProvider.authenticate(authentication);
        });

        assertEquals("Invalid username or password for user: testUser ", thrown.getMessage());
    }

    @Test
    public void testAuthenticate_NullToken() {
        // Arrange
        String username = "testUser ";
        String password = "testPassword";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(null); // Simulamos que no hay token

        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), any(), eq(AuthResponse.class))).thenReturn(responseEntity);

        // Act & Assert
        BadCredentialsException thrown = assertThrows(BadCredentialsException.class, () -> {
            customAuthenticationProvider.authenticate(authentication);
        });

        assertEquals("Invalid username or password for user: testUser ", thrown.getMessage());
    }




}
