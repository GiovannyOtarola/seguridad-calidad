package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.TokenStore;

import static org.junit.jupiter.api.Assertions.*;

class TokenStoreTest {

    @Test
    void testGetAndSetToken() {
        // Dado: Creamos una instancia de TokenStore
        TokenStore tokenStore = new TokenStore();
        
        // Dado: Definimos un token
        String expectedToken = "abc123token";

        // Cuando: Asignamos el token al objeto
        tokenStore.setToken(expectedToken);

        // Entonces: Verificamos que el token asignado es el mismo que se puede obtener
        assertEquals(expectedToken, tokenStore.getToken());
    }

    @Test
    void testTokenInitiallyNull() {
        // Dado: Creamos una nueva instancia de TokenStore
        TokenStore tokenStore = new TokenStore();
        
        // Entonces: Verificamos que el valor inicial del token es null
        assertNull(tokenStore.getToken());
    }
}
