package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.Receta;

import static org.junit.jupiter.api.Assertions.*;

class RecetaTest {

    @Test
    void testRecetaConstructorYGettersSetters() {
        // Dado: Creamos una instancia de Receta usando el constructor
        Receta receta = new Receta();

        // Damos valores a los campos usando los setters
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

        // Cuando: Obtenemos los valores de la receta usando los getters
        Long id = receta.getId();
        String nombre = receta.getNombre();
        String tipoCocina = receta.getTipoCocina();
        String paisOrigen = receta.getPaisOrigen();
        String dificultad = receta.getDificultad();
        String ingredientes = receta.getIngredientes();
        String instrucciones = receta.getInstrucciones();
        int tiempoCoccion = receta.getTiempoCoccion();
        String fotografiaUrl = receta.getFotografiaUrl();
        String urlVideo = receta.getUrlVideo();

        // Entonces: Verificamos que los valores sean los esperados
        assertEquals(1L, id);
        assertEquals("Torta de Chocolate", nombre);
        assertEquals("Pastelería", tipoCocina);
        assertEquals("Francia", paisOrigen);
        assertEquals("Media", dificultad);
        assertEquals("Harina, Chocolate, Azúcar", ingredientes);
        assertEquals("Mezclar los ingredientes y hornear", instrucciones);
        assertEquals(30, tiempoCoccion);
        assertEquals("http://url.com/foto.jpg", fotografiaUrl);
        assertEquals("http://url.com/video.mp4", urlVideo);
    }
}