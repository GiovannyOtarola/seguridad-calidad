package com.duoc.seguridad_calidad;

import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.ComentarioValoracionView;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ComentarioValoracionViewTest {

    @Test
    void testComentarioValoracionView() {
        // Dado: Creamos un mock de la interfaz ComentarioValoracionView
        ComentarioValoracionView comentarioValoracionView = mock(ComentarioValoracionView.class);

        // Configuramos el comportamiento esperado para los métodos de la interfaz
        when(comentarioValoracionView.getId()).thenReturn(1L);
        when(comentarioValoracionView.getComentario()).thenReturn("Excelente receta");
        when(comentarioValoracionView.getValoracion()).thenReturn(5L);
        when(comentarioValoracionView.getRecetaId()).thenReturn(100L);

        // Cuando: Obtenemos los valores a través de los métodos de la interfaz
        Long id = comentarioValoracionView.getId();
        String comentario = comentarioValoracionView.getComentario();
        Long valoracion = comentarioValoracionView.getValoracion();
        Long recetaId = comentarioValoracionView.getRecetaId();

        // Entonces: Verificamos que los valores son los esperados
        assertEquals(1L, id);
        assertEquals("Excelente receta", comentario);
        assertEquals(5L, valoracion);
        assertEquals(100L, recetaId);

        // Verificamos que los métodos fueron llamados
        verify(comentarioValoracionView).getId();
        verify(comentarioValoracionView).getComentario();
        verify(comentarioValoracionView).getValoracion();
        verify(comentarioValoracionView).getRecetaId();
    }
}
