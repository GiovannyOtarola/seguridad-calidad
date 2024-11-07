package com.duoc.seguridad_calidad.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.duoc.seguridad_calidad.model.Receta;
import com.duoc.seguridad_calidad.model.TokenStore;
import com.duoc.seguridad_calidad.model.User;


@Controller
public class HomeController {

    String url = "http://localhost:8080";

    private TokenStore tokenStore;
    
    @Autowired
    public void RecetaController(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @GetMapping("/home")
    public String home(Model model) {
        final var restTemplate = new RestTemplate();

        // Realiza la solicitud GET al backend para obtener el mapa de respuesta
    ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url.concat("/public/home"), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});

        // Extrae los datos del mapa
        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null) {
            model.addAttribute("recetasRecientes", responseBody.get("recetasRecientes"));
            model.addAttribute("recetasPopulares", responseBody.get("recetasPopulares"));
            model.addAttribute("banners", responseBody.get("banners"));
        }

        return "home";
    }

    @GetMapping("/buscar")
    public String buscarRecetas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "tipoCocina", required = false) String tipoCocina,
            @RequestParam(value = "paisOrigen", required = false) String paisOrigen,
            @RequestParam(value = "dificultad", required = false) String dificultad,
            Model model) {

        // Construye la URL de búsqueda con los parámetros
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/public/buscar")
                .queryParam("nombre", nombre)
                .queryParam("tipoCocina", tipoCocina)
                .queryParam("paisOrigen", paisOrigen)
                .queryParam("dificultad", dificultad);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Receta>> response = restTemplate.exchange(
            builder.toUriString(), 
            HttpMethod.GET, 
            null, 
            new ParameterizedTypeReference<List<Receta>>() {}
        );


        // Agrega los resultados al modelo para mostrar en la vista
        model.addAttribute("resultados", response.getBody());

        return "buscarRecetas";
    }
    
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new User()); // Crea un usuario vacío para el formulario
        return "registro"; // Debe coincidir con el nombre de tu plantilla HTML
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") User user, Model model) {
        try {
            // Realiza la solicitud POST al backend para registrar el usuario
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Convierte el objeto User a JSON
            HttpEntity<User> request = new HttpEntity<>(user, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                url.concat("/public/registro"), 
                HttpMethod.POST, 
                request, 
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("mensaje", "Registro exitoso, por favor inicie sesión.");
                return "login";  // Redirige al login después del registro exitoso
            } else {
                model.addAttribute("error", "Error al registrar el usuario.");
                return "registro";  // Si hubo un error, vuelve a mostrar el formulario
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            return "registro";  // Si hubo un error, vuelve a mostrar el formulario
        }
    }

    @GetMapping("/publicar")
    public String mostrarFormularioPublicar(Model model) {
        model.addAttribute("receta", new Receta()); // Crea un usuario vacío para el formulario
        return "publicar"; 
    }

    @PostMapping("/publicar")
    public String registrarReceta(@ModelAttribute("receta") Receta receta, Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Crear encabezados para enviar JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Obtener el token del TokenStore
            String token = tokenStore.getToken(); 
            headers.set("Authorization", "Bearer " + token);

            // Crear la entidad de la solicitud
            HttpEntity<Receta> request = new HttpEntity<>(receta, headers);

            // Enviar solicitud al backend
            ResponseEntity<String> response = restTemplate.exchange(
                url.concat("/private/publicar"), 
                HttpMethod.POST, 
                request, 
                String.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                model.addAttribute("mensaje", "Receta creada exitosamente");
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Error al crear la receta.");
                return "publicar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar la receta: " + e.getMessage());
            return "publicar";
        }
    }
    
    
}
