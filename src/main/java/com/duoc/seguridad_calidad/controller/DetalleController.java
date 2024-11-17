package com.duoc.seguridad_calidad.controller;


import com.duoc.seguridad_calidad.model.ComentarioValoracion;
import com.duoc.seguridad_calidad.model.ComentarioValoracionView;
import com.duoc.seguridad_calidad.model.Receta;
import com.duoc.seguridad_calidad.model.TokenStore;
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

@Controller
public class DetalleController {

    String url = "http://localhost:8080";

    private TokenStore tokenStore;

    public DetalleController(TokenStore tokenStore) {
        super();
        this.tokenStore = tokenStore;
    }

    @GetMapping("/recetas/{id}/detalle")
    public String getRecetaDetalle(@PathVariable Long id, Model model) {

        final var restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + this.tokenStore.getToken());  // Agregar prefijo "Bearer "
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Construir la URL para la receta específica
        String detalleUrl = url + "/private/recetas/" + id + "/detalle";

        try {
            // Hacer la solicitud GET al backend y parsear la respuesta como un objeto de tipo Receta
            ResponseEntity<Receta> response = restTemplate.exchange(detalleUrl, HttpMethod.GET, entity, Receta.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Pasar los detalles de la receta a la vista
                model.addAttribute("detalles", response.getBody());
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
            headers.set("Authorization", "Bearer " + token);

            // Construir el cuerpo de la solicitud
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("videoUrl", videoUrl);

            // Crear la entidad de la solicitud
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Enviar solicitud POST al backend
            String backendUrl = url.concat("/private/recetas/" + id + "/agregarVideo");
            ResponseEntity<String> response = restTemplate.exchange(
                    backendUrl, 
                    HttpMethod.POST, 
                    requestEntity, 
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                redirectAttributes.addFlashAttribute("successMessage", "Video agregado exitosamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al agregar el video.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al agregar el video: " + e.getMessage());
        }

        // Redirige de nuevo al detalle de la receta
        return "redirect:/recetas/" + id + "/detalle";
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
            headers.set("Authorization", "Bearer " + token);

            // Crear el cuerpo de la solicitud con los valores recibidos
            ComentarioValoracion comentarioValoracion = new ComentarioValoracion();
            comentarioValoracion.setRecetaId(id);  // Aquí ya no es necesario setear Receta
            comentarioValoracion.setValoracion(valoracion);
            comentarioValoracion.setComentario(comentario);

            // Crear la entidad de la solicitud
            HttpEntity<ComentarioValoracion> requestEntity = new HttpEntity<>(comentarioValoracion, headers);

            // Enviar la solicitud POST al backend
            String backendUrl = url.concat("/private/recetas/" + id + "/guardarComentarioValoracion");
            ResponseEntity<ComentarioValoracionView> response = restTemplate.exchange(
                    backendUrl, 
                    HttpMethod.POST, 
                    requestEntity, 
                    ComentarioValoracionView.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                redirectAttributes.addFlashAttribute("successMessage", "Comentario y valoración guardados correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el comentario y valoración.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el comentario y valoración: " + e.getMessage());
        }

        // Redirigir de vuelta al detalle de la receta
        return "redirect:/recetas/" + id + "/detalle";
    }

}
