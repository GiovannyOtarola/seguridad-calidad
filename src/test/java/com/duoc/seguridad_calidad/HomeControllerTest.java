package com.duoc.seguridad_calidad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.duoc.seguridad_calidad.controller.HomeController;
import com.duoc.seguridad_calidad.model.Banner;
import com.duoc.seguridad_calidad.model.Receta;
import com.duoc.seguridad_calidad.model.TokenStore;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model; // Import for model assertions
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {
    
    
    @Autowired
    private MockMvc mockMvc;  // MockMvc para simular las solicitudes HTTP

    @MockBean
    private RestTemplate restTemplate;  // Mockeamos RestTemplate para evitar llamadas reales

    @MockBean
    private AuthenticationManager authenticationManager;  // Mockeamos AuthenticationManager si es necesario

    @BeforeEach
    public void setUp() {
        // Simulamos la respuesta del backend
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("recetasRecientes", Collections.singletonList(new Receta()));  // Mock de recetas recientes
        mockResponse.put("recetasPopulares", Collections.singletonList(new Receta()));  // Mock de recetas populares
        mockResponse.put("banners", Collections.singletonList(new Banner()));  // Mock de banners

        // Simulamos el comportamiento del RestTemplate para que devuelva el resultado mockeado
        when(restTemplate.exchange(
                ArgumentMatchers.contains("/home"), // URL que estamos simulando
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Simulamos la autenticación si es necesario
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testUser", "password", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(token);  // Simulamos el contexto de seguridad
    }

    @Test
    public void testHome() throws Exception {
        // Realiza la prueba en el controlador
        mockMvc.perform(get("/home"))  // Solicitud GET al controlador
                .andExpect(status().isOk())  // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(view().name("home"))  // Verifica que la vista sea "home"
                .andExpect(model().attribute("recetasRecientes", notNullValue()))  // Verifica que el modelo tiene "recetasRecientes"
                .andExpect(model().attribute("recetasPopulares", notNullValue()))  // Verifica que el modelo tiene "recetasPopulares"
                .andExpect(model().attribute("banners", notNullValue()));  // Verifica que el modelo tiene "banners"
    }

}
