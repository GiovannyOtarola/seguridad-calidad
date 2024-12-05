package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.Banner;

import static org.junit.jupiter.api.Assertions.*;

class BannerTest {

    @Test
    void testConstructor() {
        // Dado
        String nombre = "Promoción Especial";
        String imagenUrl = "http://example.com/image.jpg";
        String enlaceUrl = "http://example.com";

        // Cuando
        Banner banner = new Banner(nombre, imagenUrl, enlaceUrl);

        // Entonces
        assertEquals(nombre, banner.getNombre());
        assertEquals(imagenUrl, banner.getImagenUrl());
        assertEquals(enlaceUrl, banner.getEnlaceUrl());
    }

    @Test
    void testSettersAndGetters() {
        // Dado
        Banner banner = new Banner();

        // Cuando
        banner.setNombre("Oferta Especial");
        banner.setImagenUrl("http://example.com/banner.jpg");
        banner.setEnlaceUrl("http://example.com/sale");

        // Entonces
        assertEquals("Oferta Especial", banner.getNombre());
        assertEquals("http://example.com/banner.jpg", banner.getImagenUrl());
        assertEquals("http://example.com/sale", banner.getEnlaceUrl());
    }

    @Test
    void testDefaultConstructor() {
        // Dado
        Banner banner = new Banner();

        // Cuando / Entonces
        assertNull(banner.getNombre());
        assertNull(banner.getImagenUrl());
        assertNull(banner.getEnlaceUrl());
    }
}