package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.ComentarioValoracionDTO;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioValoracionDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Dado
        Long id = 1L;
        Integer valoracion = 5;
        String comentario = "Excelente receta, muy sabrosa";
        boolean aprobado = true;

        // Cuando
        ComentarioValoracionDTO comentarioValoracionDTO = new ComentarioValoracionDTO();
        comentarioValoracionDTO.setId(id);
        comentarioValoracionDTO.setValoracion(valoracion);
        comentarioValoracionDTO.setComentario(comentario);
        comentarioValoracionDTO.setAprobado(aprobado);

        // Entonces
        assertEquals(id, comentarioValoracionDTO.getId());
        assertEquals(valoracion, comentarioValoracionDTO.getValoracion());
        assertEquals(comentario, comentarioValoracionDTO.getComentario());
        assertTrue(comentarioValoracionDTO.isAprobado());
    }

 

    @Test
    void testDefaultConstructor() {
        // Dado
        ComentarioValoracionDTO comentarioValoracionDTO = new ComentarioValoracionDTO();

        // Cuando / Entonces
        assertNull(comentarioValoracionDTO.getId());
        assertNull(comentarioValoracionDTO.getValoracion());
        assertNull(comentarioValoracionDTO.getComentario());
        assertFalse(comentarioValoracionDTO.isAprobado());
    }

    @Test
    void testSettersAndGetters() {
        // Dado
        ComentarioValoracionDTO comentarioValoracionDTO = new ComentarioValoracionDTO();

        // Cuando
        comentarioValoracionDTO.setId(3L);
        comentarioValoracionDTO.setValoracion(4);
        comentarioValoracionDTO.setComentario("Receta excelente");
        comentarioValoracionDTO.setAprobado(false);

        // Entonces
        assertEquals(3L, comentarioValoracionDTO.getId());
        assertEquals(4, comentarioValoracionDTO.getValoracion());
        assertEquals("Receta excelente", comentarioValoracionDTO.getComentario());
        assertFalse(comentarioValoracionDTO.isAprobado());
    }
}
