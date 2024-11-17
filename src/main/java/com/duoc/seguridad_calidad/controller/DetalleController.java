package com.duoc.seguridad_calidad.controller;


import com.duoc.seguridad_calidad.model.ComentarioValoracion;
import com.duoc.seguridad_calidad.model.ComentarioValoracionView;
import com.duoc.seguridad_calidad.model.Receta;
import com.duoc.seguridad_calidad.model.TokenStore;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DetalleController {

    String url = "http://localhost:8080";

    private TokenStore tokenStore;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String PRIVATE_RECIPES_BASE = "/private/recetas/";
    public static final String DETAIL_PATH = "/detalle";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String SUCCESS_MESSAGE = "successMessage";

    public DetalleController(TokenStore tokenStore) {
        super();
        this.tokenStore = tokenStore;
    }

    @GetMapping("/recetas/{id}/detalle")
    public String getRecetaDetalle(@PathVariable Long id, Model model) {

        final var restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + this.tokenStore.getToken());  // Agregar prefijo "Bearer "
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Construir la URL para la receta específica
        String detalleUrl = url + PRIVATE_RECIPES_BASE + id + DETAIL_PATH;
        String comentarioValoracionUrl = url + PRIVATE_RECIPES_BASE + id + "/comentariosValoracion";

        try {
            // Hacer la solicitud GET al backend y parsear la respuesta como un objeto de tipo Receta
            ResponseEntity<Receta> response = restTemplate.exchange(detalleUrl, HttpMethod.GET, entity, Receta.class);

            ResponseEntity<List<ComentarioValoracion>> responseComentario = restTemplate.exchange(
                    comentarioValoracionUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ComentarioValoracion>>() {}
            );
            List<ComentarioValoracion> comentarios = responseComentario.getBody();




            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Pasar los detalles de la receta a la vista
                model.addAttribute("detalles", response.getBody());
                model.addAttribute("comentarios",comentarios);
            } else {
                model.addAttribute("error", "No se pudieron obtener los detalles de la receta.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al obtener los detalles de la receta: " + e.getMessage());
        }

        return "detalleReceta";
    }

    @PostMapping("/recetas/{id}/agregarVideo")
    public String agregarVideo(
            @PathVariable Long id, 
            @RequestParam String videoUrl, 
            RedirectAttributes redirectAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Crear encabezados para enviar parámetros
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Obtener el token del TokenStore
            String token = tokenStore.getToken(); 
            headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + token);

            // Construir el cuerpo de la solicitud
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("videoUrl", videoUrl);

            // Crear la entidad de la solicitud
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Enviar solicitud POST al backend
            String backendUrl = url.concat(PRIVATE_RECIPES_BASE + id + "/agregarVideo");
            ResponseEntity<String> response = restTemplate.exchange(
                    backendUrl, 
                    HttpMethod.POST, 
                    requestEntity, 
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Video agregado exitosamente.");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Error al agregar el video.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Error al agregar el video: " + e.getMessage());
        }

        // Redirige de nuevo al detalle de la receta
        return "redirect:/recetas/" + id + DETAIL_PATH;
    }


    @PostMapping("/recetas/{id}/guardarComentarioValoracion")
    public String guardarComentarioValoracion(
            @PathVariable Long id, 
            @RequestParam Integer valoracion, 
            @RequestParam String comentario,
            RedirectAttributes redirectAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Crear encabezados para enviar parámetros
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Obtener el token del TokenStore
            String token = tokenStore.getToken(); 
            headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + token);

            // Crear el cuerpo de la solicitud con los valores recibidos
            ComentarioValoracion comentarioValoracion = new ComentarioValoracion();
            comentarioValoracion.setRecetaId(id);  // Aquí ya no es necesario setear Receta
            comentarioValoracion.setValoracion(valoracion);
            comentarioValoracion.setComentario(comentario);

            // Crear la entidad de la solicitud
            HttpEntity<ComentarioValoracion> requestEntity = new HttpEntity<>(comentarioValoracion, headers);

            // Enviar la solicitud POST al backend
            String backendUrl = url.concat(PRIVATE_RECIPES_BASE + id + "/guardarComentarioValoracion");
            ResponseEntity<ComentarioValoracionView> response = restTemplate.exchange(
                    backendUrl, 
                    HttpMethod.POST, 
                    requestEntity, 
                    ComentarioValoracionView.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Comentario y valoración guardados correctamente.");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Error al guardar el comentario y valoración.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Error al guardar el comentario y valoración: " + e.getMessage());
        }

        // Redirigir de vuelta al detalle de la receta
        return "redirect:/recetas/" + id + DETAIL_PATH;
    }

}
