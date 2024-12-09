package com.duoc.seguridad_calidad;

import com.duoc.seguridad_calidad.controller.DetalleController;
import com.duoc.seguridad_calidad.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DetalleController.class)
public class DetalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenStore tokenStore;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetRecetaDetalle_Success() throws Exception {
        Receta mockReceta = new Receta();
        List<ComentarioValoracion> mockComentarios = Collections.singletonList(new ComentarioValoracion());

        when(tokenStore.getToken()).thenReturn("testToken");
        when(restTemplate.exchange(
                eq("http://localhost:8080/private/recetas/1/detalle"),
                eq(HttpMethod.GET),
                any(),
                eq(Receta.class)
        )).thenReturn(new ResponseEntity<>(mockReceta, HttpStatus.OK));

        when(restTemplate.exchange(
                eq("http://localhost:8080/private/receta/1/comentariosValoracion"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<ComentarioValoracion>>() {})
        )).thenReturn(new ResponseEntity<>(mockComentarios, HttpStatus.OK));

        mockMvc.perform(get("/recetas/1/detalle"))
                .andExpect(status().isOk())
                .andExpect(view().name("detalleReceta"))
                .andExpect(model().attribute("detalles", mockReceta))
                .andExpect(model().attribute("comentarios", mockComentarios));
    }


        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testAgregarVideo_Success() throws Exception {
        String videoUrl = "https://example.com/video.mp4";

        // Mocking the TokenStore to return a valid token
        when(tokenStore.getToken()).thenReturn("testToken");

        // Mocking the restTemplate exchange for adding a video
        when(restTemplate.exchange(
                eq("http://localhost:8080/private/recetas/1/agregarVideo"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));

        // Perform the POST request to the endpoint
        mockMvc.perform(post("/recetas/1/agregarVideo")
                 .with(csrf())
                .param("videoUrl", videoUrl))
                .andExpect(status().is3xxRedirection()) // Expecting redirection
                .andExpect(redirectedUrl("/recetas/1/detalle")) // Ensure it redirects to the correct URL
                .andExpect(flash().attribute("successMessage", "Video agregado exitosamente.")); // Verify success message in redirect
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testGuardarComentarioValoracion_Success() throws Exception {
            Integer valoracion = 5;
            String comentario = "Great recipe!";
        
            // Mocking the TokenStore to return a valid token
            when(tokenStore.getToken()).thenReturn("testToken");
        
            // Mocking the restTemplate exchange for saving a comment and rating
            ComentarioValoracionView mockComentarioValoracionView = mock(ComentarioValoracionView.class);
            when(restTemplate.exchange(
                    eq("http://localhost:8080/private/recetas/1/guardarComentarioValoracion"),
                    eq(HttpMethod.POST),
                    any(HttpEntity.class),
                    eq(ComentarioValoracionView.class)
            )).thenReturn(new ResponseEntity<>(mockComentarioValoracionView, HttpStatus.OK));
        
            // Perform the POST request to the endpoint
            mockMvc.perform(post("/recetas/1/guardarComentarioValoracion")
                    .with(csrf())
                    .param("valoracion", valoracion.toString())
                    .param("comentario", comentario))
                    .andExpect(status().is3xxRedirection()) // Expecting redirection
                    .andExpect(redirectedUrl("/recetas/1/detalle")); // Ensure it redirects to the correct URL
                     // Verify success message in redirect
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testAgregarVideo_Error() throws Exception {
        String videoUrl = "https://example.com/video.mp4";

        // Mocking the TokenStore to return a valid token
        when(tokenStore.getToken()).thenReturn("testToken");

        // Mocking the restTemplate exchange to throw an error
        when(restTemplate.exchange(
                eq("http://localhost:8080/private/recetas/1/agregarVideo"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Perform the POST request to the endpoint
        mockMvc.perform(post("/recetas/1/agregarVideo")
                .with(csrf())
                .param("videoUrl", videoUrl))
                .andExpect(status().is3xxRedirection()) // Expecting redirection
                .andExpect(redirectedUrl("/recetas/1/detalle")) // Ensure it redirects to the correct URL
                .andExpect(flash().attribute("errorMessage", "Error al agregar el video: 400 BAD_REQUEST")); // Verify error message in redirect
        }




}