package com.duoc.seguridad_calidad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.duoc.seguridad_calidad.controller.HomeController;
import com.duoc.seguridad_calidad.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Usamos MockMvc para pruebas de controladores

    @MockBean
    private TokenStore tokenStore;

    @MockBean
    private RestTemplate restTemplate; 



    @Test
    @WithMockUser(username = "testUser", roles = {"USER"}) // Simula un usuario autenticado con un rol
    public void testHome_Success() throws Exception {
        // Datos mockeados que devolveremos en la respuesta
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("recetasRecientes", Collections.emptyList());
        mockResponse.put("recetasPopulares", Collections.emptyList());
        mockResponse.put("banners", Collections.emptyList());

        // Configuramos el mock para el RestTemplate para evitar la conexión real
        when(restTemplate.exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {}))
        ).thenReturn(ResponseEntity.ok(mockResponse));  // Respuesta mockeada

        // Realizamos la solicitud al controlador usando MockMvc
        mockMvc.perform(get("/home")
                .header(HttpHeaders.AUTHORIZATION, "Bearer fake-jwt-token-with-role")) // Simula el encabezado de autenticación
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("recetasRecientes", hasSize(0)))  // Verifica que la lista esté vacía
                .andExpect(model().attribute("recetasPopulares", hasSize(0)))  // Verifica que la lista esté vacía
                .andExpect(model().attribute("banners", hasSize(0)))  // Verifica que la lista esté vacía
                .andExpect(model().attribute("role", "ROLE_USER"));  // Verifica que el rol esté presente en el modelo

        // Verificamos que el RestTemplate haya sido invocado correctamente
        verify(restTemplate).exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {}));
    }
        

}