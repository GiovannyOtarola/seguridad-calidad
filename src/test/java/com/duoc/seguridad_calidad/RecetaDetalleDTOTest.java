package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.ComentarioValoracionView;
import com.duoc.seguridad_calidad.model.Receta;
import com.duoc.seguridad_calidad.model.RecetaDetalleDTO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecetaDetalleDTOTest {

    @Test
    void testRecetaDetalleDTOConstructorYGettersSetters() {
        // Dado: Creamos una instancia de Receta
        Receta receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Torta de Chocolate");
        receta.setTipoCocina("Pastelería");
        receta.setPaisOrigen("Francia");
        receta.setDificultad("Media");
        receta.setIngredientes("Harina, Chocolate, Azúcar");
        receta.setInstrucciones("Mezclar los ingredientes y hornear");
        receta.setTiempoCoccion(30);
        receta.setFotografiaUrl("http://url.com/foto.jpg");
        receta.setUrlVideo("http://url.com/video.mp4");

        // Dado: Creamos una lista de ComentarioValoracionView
        ComentarioValoracionView comentario1 = new ComentarioValoracionView() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getComentario() {
                return "Deliciosa receta!";
            }

            @Override
            public Long getValoracion() {
                return 5L;
            }

            @Override
            public Long getRecetaId() {
                return 1L;
            }
        };

        ComentarioValoracionView comentario2 = new ComentarioValoracionView() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public String getComentario() {
                return "Muy fácil de hacer.";
            }

            @Override
            public Long getValoracion() {
                return 4L;
            }

            @Override
            public Long getRecetaId() {
                return 1L;
            }
        };

        List<ComentarioValoracionView> comentarios = Arrays.asList(comentario1, comentario2);

        // Cuando: Creamos la instancia de RecetaDetalleDTO
        RecetaDetalleDTO recetaDetalleDTO = new RecetaDetalleDTO();
        recetaDetalleDTO.setReceta(receta);
        recetaDetalleDTO.setComentarios(comentarios);

        // Entonces: Verificamos que los valores se hayan asignado correctamente

        // Verificación de los datos de la receta
        assertEquals(1L, recetaDetalleDTO.getReceta().getId());
        assertEquals("Torta de Chocolate", recetaDetalleDTO.getReceta().getNombre());
        assertEquals("Pastelería", recetaDetalleDTO.getReceta().getTipoCocina());
        assertEquals("Francia", recetaDetalleDTO.getReceta().getPaisOrigen());
        assertEquals("Media", recetaDetalleDTO.getReceta().getDificultad());
        assertEquals("Harina, Chocolate, Azúcar", recetaDetalleDTO.getReceta().getIngredientes());
        assertEquals("Mezclar los ingredientes y hornear", recetaDetalleDTO.getReceta().getInstrucciones());
        assertEquals(30, recetaDetalleDTO.getReceta().getTiempoCoccion());
        assertEquals("http://url.com/foto.jpg", recetaDetalleDTO.getReceta().getFotografiaUrl());
        assertEquals("http://url.com/video.mp4", recetaDetalleDTO.getReceta().getUrlVideo());

        // Verificación de los comentarios
        assertNotNull(recetaDetalleDTO.getComentarios());
        assertEquals(2, recetaDetalleDTO.getComentarios().size());

        // Verificación del primer comentario
        assertEquals(1L, recetaDetalleDTO.getComentarios().get(0).getId());
        assertEquals("Deliciosa receta!", recetaDetalleDTO.getComentarios().get(0).getComentario());
        assertEquals(5L, recetaDetalleDTO.getComentarios().get(0).getValoracion());

        // Verificación del segundo comentario
        assertEquals(2L, recetaDetalleDTO.getComentarios().get(1).getId());
        assertEquals("Muy fácil de hacer.", recetaDetalleDTO.getComentarios().get(1).getComentario());
        assertEquals(4L, recetaDetalleDTO.getComentarios().get(1).getValoracion());
    }
}