package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.AuthResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthResponseTest {

    @Test
    void testAuthResponseConstructorAndGetters() {
        // Datos de prueba
        String token = "sampleToken123";
        String role = "admin";

        // Crear instancia de AuthResponse
        AuthResponse authResponse = new AuthResponse(token, role);

        // Verificar que los valores sean los correctos
        assertEquals(token, authResponse.getToken(), "El token no es el esperado");
        assertEquals(role, authResponse.getRole(), "El rol no es el esperado");
    }

}