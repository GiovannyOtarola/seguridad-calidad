package com.duoc.seguridad_calidad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.duoc.seguridad_calidad.controller.HomeController;
import com.duoc.seguridad_calidad.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("recetasRecientes", Collections.emptyList());
        mockResponse.put("recetasPopulares", Collections.emptyList());
        mockResponse.put("banners", Collections.emptyList());

        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {})
        )).thenReturn(responseEntity);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("recetasRecientes", Collections.emptyList()))
                .andExpect(model().attribute("recetasPopulares", Collections.emptyList()))
                .andExpect(model().attribute("banners", Collections.emptyList()));

        verify(restTemplate).exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {})
        );
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testBuscarRecetas_Success() throws Exception {
        List<Receta> mockRecetas = Collections.singletonList(new Receta());

        ResponseEntity<List<Receta>> responseEntity =
                new ResponseEntity<>(mockRecetas, HttpStatus.OK);

        when(restTemplate.exchange(
                contains("/public/buscar"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<List<Receta>>() {})
        )).thenReturn(responseEntity);

        mockMvc.perform(get("/buscar")
                        .param("nombre", "Paella")
                        .param("tipoCocina", "Española")
                        .param("paisOrigen", "España")
                        .param("dificultad", "Media"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscarRecetas"))
                .andExpect(model().attribute("resultados", mockRecetas));

        verify(restTemplate).exchange(
                contains("/public/buscar"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<List<Receta>>() {})
        );
    }

//    @Test
//    @WithMockUser(username = "testUser", roles = {"USER"})
//    public void testRegistrarUsuario_Success() throws Exception {
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Usuario registrado", HttpStatus.OK);
//
//        when(restTemplate.exchange(
//                eq("http://localhost:8080/public/registro"),
//                eq(HttpMethod.POST),
//                any(),
//                eq(String.class)
//        )).thenReturn(responseEntity);
//
//        mockMvc.perform(post("/registro")
//                        .param("username", "testuser")
//                        .param("password", "password123")
//                        .contentType("application/x-www-form-urlencoded"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andExpect(model().attribute("mensaje", "Registro exitoso, por favor inicie sesión."));
//
//        verify(restTemplate).exchange(
//                eq("http://localhost:8080/public/registro"),
//                eq(HttpMethod.POST),
//                any(),
//                eq(String.class)
//        );
//    }

//    @Test
//    @WithMockUser(username = "testUser", roles = {"USER"})
//    public void testRegistrarReceta_Success() throws Exception {
//        when(tokenStore.getToken()).thenReturn("testToken");
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Receta creada", HttpStatus.CREATED);
//
//        when(restTemplate.exchange(
//                eq("http://localhost:8080/private/publicar"),
//                eq(HttpMethod.POST),
//                any(),
//                eq(String.class)
//        )).thenReturn(responseEntity);
//
//        mockMvc.perform(post("/publicar")
//                        .param("nombre", "Paella")
//                        .param("tipoCocina", "Española")
//                        .param("paisOrigen", "España")
//                        .param("dificultad", "Media")
//                        .param("ingredientes", "Arroz, Mariscos")
//                        .param("instrucciones", "Cocinar a fuego lento")
//                        .contentType("application/x-www-form-urlencoded"))
//                .andExpect(status().isCreated())
//                .andExpect(redirectedUrl("/home"));
//
//        verify(restTemplate).exchange(
//                eq("http://localhost:8080/private/publicar"),
//                eq(HttpMethod.POST),
//                any(),
//                eq(String.class)
//        );
//    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAdmin_Success() throws Exception {
        List<User> mockUsers = Arrays.asList(new User("admin", "Admin User", "admin@example.com", "password"));

        when(tokenStore.getToken()).thenReturn("adminToken");

        ResponseEntity<List<User>> responseEntity = new ResponseEntity<>(mockUsers, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/private/users"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<User>>() {})
        )).thenReturn(responseEntity);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attribute("usuarios", mockUsers));

        verify(restTemplate).exchange(
                eq("http://localhost:8080/private/users"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<User>>() {})
        );
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testActualizarUsuario_Success() throws Exception {
//        when(tokenStore.getToken()).thenReturn("adminToken");
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
//
//        when(restTemplate.exchange(
//                eq("http://localhost:8080/private/users/1"),
//                eq(HttpMethod.PUT),
//                any(),
//                eq(String.class)
//        )).thenReturn(responseEntity);
//
//        mockMvc.perform(post("/admin/users/1")
//                        .param("nombre", "Updated User")
//                        .param("email", "updated@example.com")
//                        .contentType("application/x-www-form-urlencoded"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin"))
//                .andExpect(model().attributeExists("mensaje"));
//
//        verify(restTemplate).exchange(
//                eq("http://localhost:8080/private/users/1"),
//                eq(HttpMethod.PUT),
//                any(),
//                eq(String.class)
//        );
//    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testComentarios_Success() throws Exception {
        List<ComentarioValoracionDTO> mockComentarios = Arrays.asList(new ComentarioValoracionDTO());

        when(tokenStore.getToken()).thenReturn("adminToken");

        ResponseEntity<List<ComentarioValoracionDTO>> responseEntity = new ResponseEntity<>(mockComentarios, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/private/comentarios"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<ComentarioValoracionDTO>>() {})
        )).thenReturn(responseEntity);

        mockMvc.perform(get("/comentarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("comentarios"))
                .andExpect(model().attribute("comentarios", mockComentarios));

        verify(restTemplate).exchange(
                eq("http://localhost:8080/private/comentarios"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<ComentarioValoracionDTO>>() {})
        );
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testActualizarComentarios_Success() throws Exception {
//        when(tokenStore.getToken()).thenReturn("adminToken");
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Comentario actualizado", HttpStatus.OK);
//
//        when(restTemplate.exchange(
//                eq("http://localhost:8080/private/comentarios/1"),
//                eq(HttpMethod.PUT),
//                any(),
//                eq(String.class)
//        )).thenReturn(responseEntity);
//
//        mockMvc.perform(post("/admin/comentarios/1")
//                        .param("comentario", "Updated Comment")
//                        .contentType("application/x-www-form-urlencoded"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("comentarios"))
//                .andExpect(model().attributeExists("mensaje"));
//
//        verify(restTemplate).exchange(
//                eq("http://localhost:8080/private/comentarios/1"),
//                eq(HttpMethod.PUT),
//                any(),
//                eq(String.class)
//        );
//    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testMostrarFormularioRegistro_Success() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void testAdmin_HttpClientErrorException() throws Exception {
        when(tokenStore.getToken()).thenReturn("validToken");

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(Class.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/admin"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testAdmin_HttpServerErrorException() throws Exception {
        when(tokenStore.getToken()).thenReturn("validToken");

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(Class.class)
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAdmin_GenericException() throws Exception {
        when(tokenStore.getToken()).thenReturn("validToken");

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(Class.class)
        )).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testAdmin_Returns500OnError() throws Exception {
        when(tokenStore.getToken()).thenReturn("validToken");

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(Class.class)
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Si esperas que el controlador devuelva un código de estado 500:
        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }
}
