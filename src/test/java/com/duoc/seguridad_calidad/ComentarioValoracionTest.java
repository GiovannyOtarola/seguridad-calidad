package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.ComentarioValoracion;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioValoracionTest {

    @Test
    void testConstructorAndGetters() {
        // Dado
        Long id = 1L;
        Long recetaId = 101L;
        Integer valoracion = 5;
        String comentario = "Excelente receta, muy sabrosa";

        // Cuando
        ComentarioValoracion comentarioValoracion = new ComentarioValoracion();
        comentarioValoracion.setId(id);
        comentarioValoracion.setRecetaId(recetaId);
        comentarioValoracion.setValoracion(valoracion);
        comentarioValoracion.setComentario(comentario);

        // Entonces
        assertEquals(id, comentarioValoracion.getId());
        assertEquals(recetaId, comentarioValoracion.getRecetaId());
        assertEquals(valoracion, comentarioValoracion.getValoracion());
        assertEquals(comentario, comentarioValoracion.getComentario());
    }

    @Test
    void testDefaultConstructor() {
        // Dado
        ComentarioValoracion comentarioValoracion = new ComentarioValoracion();

        // Cuando / Entonces
        assertNull(comentarioValoracion.getId());
        assertNull(comentarioValoracion.getRecetaId());
        assertNull(comentarioValoracion.getValoracion());
        assertNull(comentarioValoracion.getComentario());
    }

    @Test
    void testSettersAndGetters() {
        // Dado
        ComentarioValoracion comentarioValoracion = new ComentarioValoracion();

        // Cuando
        comentarioValoracion.setId(2L);
        comentarioValoracion.setRecetaId(200L);
        comentarioValoracion.setValoracion(4);
        comentarioValoracion.setComentario("Muy buena receta");

        // Entonces
        assertEquals(2L, comentarioValoracion.getId());
        assertEquals(200L, comentarioValoracion.getRecetaId());
        assertEquals(4, comentarioValoracion.getValoracion());
        assertEquals("Muy buena receta", comentarioValoracion.getComentario());
    }
}
