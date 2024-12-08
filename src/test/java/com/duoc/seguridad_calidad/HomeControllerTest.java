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
        private MockMvc mockMvc;
    
        @MockBean
        private TokenStore tokenStore;
    
        @MockBean
        private RestTemplate restTemplate;
    
        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testHome_Success() throws Exception {
            // Datos mockeados
            Map<String, Object> mockResponse = new HashMap<>();
            mockResponse.put("recetasRecientes", Collections.emptyList());
            mockResponse.put("recetasPopulares", Collections.emptyList());
            mockResponse.put("banners", Collections.emptyList());
    
            // Configurar el mock de RestTemplate
            ResponseEntity<Map<String, Object>> responseEntity = 
                new ResponseEntity<>(mockResponse, HttpStatus.OK);
    
            when(restTemplate.exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {})
            )).thenReturn(responseEntity);
    
            // Realizar la prueba
            mockMvc.perform(get("/home"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().attribute("recetasRecientes", hasSize(0)))
                    .andExpect(model().attribute("recetasPopulares", hasSize(0)))
                    .andExpect(model().attribute("banners", hasSize(0)));
    
            // Verificar que el método exchange fue llamado
            verify(restTemplate).exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {})
            );
        }

}