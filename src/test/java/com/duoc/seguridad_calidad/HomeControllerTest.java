package com.duoc.seguridad_calidad;


import com.duoc.seguridad_calidad.controller.HomeController;
import com.duoc.seguridad_calidad.model.*;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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



    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testMostrarFormularioRegistro_Success() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    public void testAdmin_HttpClientErrorException() throws Exception {
        when(tokenStore.getToken()).thenReturn("validToken");
    
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<User>>() {})
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
    
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())  // Expecting a 400 Bad Request
                .andExpect(view().name("admin"))    // Ensure it still renders the admin page
                .andExpect(model().attribute("error", "Error al cargar los usuarios: 400 BAD_REQUEST"));  // Check if the error message is set
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    public void testAdmin_HttpServerErrorException() throws Exception {
        when(tokenStore.getToken()).thenReturn("validToken");
    
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<User>>() {}))
        ).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())  // Expecting 200 OK since we're rendering the error page
                .andExpect(view().name("admin"))  // Ensure the 'admin' view is returned
                .andExpect(model().attribute("error", "Error al cargar los usuarios: 500 INTERNAL_SERVER_ERROR"));  // Check error message in the model
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    public void testAdmin_GenericException() throws Exception {
        when(tokenStore.getToken()).thenReturn("validToken");
    
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<User>>() {})
        )).thenThrow(new RuntimeException("Unexpected error"));
    
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())  
                .andExpect(view().name("admin"))  // Ensure it still renders the admin page
                .andExpect(model().attribute("error", "Error inesperado al cargar los usuarios: Unexpected error"));  // Check error message
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

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testHome_HttpClientErrorException() throws Exception {
        when(restTemplate.exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {})
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeDoesNotExist("recetasRecientes"))
                .andExpect(model().attributeDoesNotExist("recetasPopulares"))
                .andExpect(model().attributeDoesNotExist("banners"));
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testHome_HttpServerErrorException() throws Exception {
        when(restTemplate.exchange(
                eq("http://localhost:8080/public/home"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {})
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeDoesNotExist("recetasRecientes"))
                .andExpect(model().attributeDoesNotExist("recetasPopulares"))
                .andExpect(model().attributeDoesNotExist("banners"));
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testBuscarRecetas_EmptyResults() throws Exception {
        ResponseEntity<List<Receta>> responseEntity =
                new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

        when(restTemplate.exchange(
                contains("/public/buscar"),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<List<Receta>>() {})
        )).thenReturn(responseEntity);

        mockMvc.perform(get("/buscar")
                        .param("nombre", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscarRecetas"))
                .andExpect(model().attribute("resultados", Collections.emptyList()));
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testBuscarRecetas_HttpClientErrorException() throws Exception {
            when(restTemplate.exchange(
                    contains("/public/buscar"),
                    eq(HttpMethod.GET),
                    isNull(),
                    eq(new ParameterizedTypeReference<List<Receta>>() {})
            )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        
            mockMvc.perform(get("/buscar")
                            .param("nombre", "Error"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("buscarRecetas"))
                    .andExpect(model().attribute("resultados", Collections.emptyList())) // Espera lista vacía
                    .andExpect(model().attribute("error", "Ocurrió un error al buscar recetas. Intente nuevamente."));
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testMostrarFormularioPublicar() throws Exception {
        // Realizamos la solicitud GET a /publicar
        mockMvc.perform(get("/publicar"))
                .andExpect(status().isOk())  // Esperamos que la respuesta sea 200 OK
                .andExpect(MockMvcResultMatchers.view().name("publicar"))  // Verificamos la vista
                .andExpect(model().attributeExists("receta"))  // Verificamos que 'receta' está presente en el modelo
                .andExpect(model().attribute("receta", hasProperty("nombre", equalTo(null)))) // Comprobamos que 'nombre' sea null
                .andExpect(model().attribute("receta", hasProperty("tipoCocina", equalTo(null)))) // 'tipoCocina' debería ser null
                .andExpect(model().attribute("receta", hasProperty("paisOrigen", equalTo(null)))) // 'paisOrigen' debería ser null
                .andExpect(model().attribute("receta", hasProperty("dificultad", equalTo(null)))) // 'dificultad' debería ser null
                .andExpect(model().attribute("receta", hasProperty("ingredientes", equalTo(null)))) // 'ingredientes' debería ser null
                .andExpect(model().attribute("receta", hasProperty("instrucciones", equalTo(null)))) // 'instrucciones' debería ser null
                .andExpect(model().attribute("receta", hasProperty("fotografiaUrl", equalTo(null)))); // 'fotografiaUrl' debería ser null
    }

       @Test
       @WithMockUser(username = "testUser", roles = {"USER"})
        public void testRegistrarUsuarioSuccess() throws Exception {
        User user = new User("testUser", "Test User", "test@example.com", "password123");
        
        // Mock the RestTemplate response for the registration
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Perform the POST request to /registro
        mockMvc.perform(post("/registro")
                .with(csrf())
                .flashAttr("usuario", user))
                .andExpect(status().is3xxRedirection())  // Expecting a redirect
                .andExpect(redirectedUrl("/login"));  // Check the redirection URL
                
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testRegistrarUsuarioFailure() throws Exception {
        User user = new User("testUser", "Test User", "test@example.com", "password123");
        
        // Mock failure response
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Perform the POST request
        mockMvc.perform(post("/registro")
                .with(csrf())
                .flashAttr("usuario", user))
                .andExpect(status().isOk())  // Should return 200 since we are still on the same page
                .andExpect(view().name("registro"))  // Should show the "registro" view again
                .andExpect(model().attribute("error", "Error al registrar el usuario."));  // Error message
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testRegistrarRecetaSuccess() throws Exception {
        Receta receta = new Receta();
        receta.setNombre("Test Recipe");
        receta.setIngredientes("Ingredients");
        receta.setInstrucciones("Instructions");
        receta.setTipoCocina("Cuisine");

        // Mock the RestTemplate response for successful recipe creation
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // Perform the POST request to /publicar
        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", receta))
                .andExpect(status().is3xxRedirection())  // Expecting a redirect after creation
                .andExpect(redirectedUrl("/home"));  // Check the redirection URL
                
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"USER"})
        public void testRegistrarRecetaFailure() throws Exception {
        Receta receta = new Receta();
        receta.setNombre("Test Recipe");
        receta.setIngredientes("Ingredients");
        receta.setInstrucciones("Instructions");
        receta.setTipoCocina("Cuisine");

        // Mock failure response
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Perform the POST request
        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", receta))
                .andExpect(status().isOk())  // Should stay on the same page after failure
                .andExpect(view().name("publicar"))  // Check the "publicar" view is returned
                .andExpect(model().attribute("error", "Error al crear la receta."));
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"ADMIN"})
        public void testActualizarUsuario_Success() throws Exception {
            Integer userId = 1;
            User user = new User(); // Asigna los valores necesarios a tu objeto User
    
            // Simular el token
            when(tokenStore.getToken()).thenReturn("mocked_token");
    
            // Simular la respuesta del RestTemplate
            ResponseEntity<String> responseEntity = new ResponseEntity<>("{}", HttpStatus.OK);
            when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(responseEntity);
    
            mockMvc.perform(post("/admin/users/{id}", userId)
                .with(csrf())
                    .param("usuario", "value")) // Agrega los parámetros necesarios
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin"));
                    
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"ADMIN"})
        public void testActualizarUsuario_Error() throws Exception {
            Integer userId = 1;
            User user = new User(); // Asigna los valores necesarios a tu objeto User
    
            // Simular el token
            when(tokenStore.getToken()).thenReturn("mocked_token");
    
            // Simular una respuesta de error del RestTemplate
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
            when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(responseEntity);
    
            mockMvc.perform(post("/admin/users/{id}", userId)
                    .with(csrf())
                    .param("usuario", "value")) // Agrega los parámetros necesarios
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin")) // Asegúrate de que el nombre de la vista sea correcto
                    .andExpect(model().attribute("error", "Error al actualizar el usuario."));
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"ADMIN"})
        public void testActualizarUsuario_NoToken() throws Exception {
            Integer userId = 1;
            User user = new User(); // Asigna los valores necesarios a tu objeto User
    
            // Simular que no hay token
            when(tokenStore.getToken()).thenReturn(null);
    
            mockMvc.perform(post("/admin/users/{id}", userId)
                    .with(csrf())
                    .param("usuario", "value")) // Agrega los parámetros necesarios
                    .andExpect(status().isOk())
                    .andExpect(view().name("login")) // Asegúrate de que el nombre de la vista sea correcto
                    .andExpect(model().attribute("error", "Token de autenticación no disponible."));
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"ADMIN"})
        public void testActualizarComentarios_Success() throws Exception {
        Long comentarioId = 1L;
        ComentarioValoracionDTO comentarioValoracion = new ComentarioValoracionDTO(); 

        // Simular el token
        when(tokenStore.getToken()).thenReturn("mocked_token");

        // Simular la respuesta del RestTemplate
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{}", HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(post("/admin/comentarios/{id}", comentarioId)
                .with(csrf())
                .param("comentario", "value")) // Agrega los parámetros necesarios
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comentarios"));
               
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    public void testActualizarComentarios_Error() throws Exception {
        Long comentarioId = 1L;
        ComentarioValoracionDTO comentarioValoracion = new ComentarioValoracionDTO(); 

        // Simular el token
        when(tokenStore.getToken()).thenReturn("mocked_token");

        // Simular una respuesta de error del RestTemplate
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(post("/admin/comentarios/{id}", comentarioId)
                .with(csrf())
                .param("comentario", "value")) // Agrega los parámetros necesarios
                .andExpect(status().isOk())
                .andExpect(view().name("comentarios")) // Asegúrate de que el nombre de la vista sea correcto
                .andExpect(model().attribute("error", "Error al actualizar el comentario."));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    public void testActualizarComentarios_NoToken() throws Exception {
        Long comentarioId = 1L;
        ComentarioValoracionDTO comentarioValoracion = new ComentarioValoracionDTO(); // Asigna los valores necesarios a tu objeto ComentarioValoracionDTO

        // Simular que no hay token
        when(tokenStore.getToken()).thenReturn(null);

        mockMvc.perform(post("/admin/comentarios/{id}", comentarioId)
                .with(csrf())
                .param("comentario", "value")) // Agrega los parámetros necesarios
                .andExpect(status().isOk())
                .andExpect(view().name("login")) // Asegúrate de que el nombre de la vista sea correcto
                .andExpect(model().attribute("error", "Token de autenticación no disponible."));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
        void testComentarios_HttpClientErrorException() throws Exception {
        // Arrange
        String expectedErrorMessage = "Error al cargar los comentarios: 400 BAD_REQUEST";
        
        // Simula una respuesta con error (ejemplo: 400 Bad Request)
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(new ParameterizedTypeReference<List<ComentarioValoracionDTO>>() {})))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Perform the GET request
        mockMvc.perform(get("/comentarios"))
                .andExpect(status().isOk()) // El método debe devolver un status 200
                .andExpect(model().attribute("error", expectedErrorMessage)) // Verifica el mensaje de error en el modelo
                .andExpect(view().name("comentarios"));  // Verifica que se devuelva la vista comentarios
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"ADMIN"})
        void testComentarios_HttpServerErrorException() throws Exception {
        // Arrange
        String expectedErrorMessage = "Error al cargar los comentarios: 500 INTERNAL_SERVER_ERROR";
        
        // Simula una respuesta con error (ejemplo: 500 Internal Server Error)
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(new ParameterizedTypeReference<List<ComentarioValoracionDTO>>() {})))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Perform the GET request
        mockMvc.perform(get("/comentarios"))
                .andExpect(status().isOk()) // El método debe devolver un status 200
                .andExpect(model().attribute("error", expectedErrorMessage)) // Verifica el mensaje de error en el modelo
                .andExpect(view().name("comentarios"));  // Verifica que se devuelva la vista comentarios
        }

        @Test
        @WithMockUser(username = "testUser", roles = {"ADMIN"})
        void testComentarios_Exception() throws Exception {
        // Arrange
        String expectedErrorMessage = "Error inesperado al cargar los comentarios: Unexpected error";
        
        // Simula un error inesperado lanzando una excepción genérica
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(new ParameterizedTypeReference<List<ComentarioValoracionDTO>>() {})))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Perform the GET request
        mockMvc.perform(get("/comentarios"))
                .andExpect(status().isOk()) // El método debe devolver un status 200
                .andExpect(model().attribute("error", expectedErrorMessage)) // Verifica el mensaje de error en el modelo
                .andExpect(view().name("comentarios"));  // Verifica que se devuelva la vista comentarios
        }

       
}
