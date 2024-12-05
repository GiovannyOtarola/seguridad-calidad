package com.duoc.seguridad_calidad.model;

public class Banner {
    
    private String nombre;
    private String imagenUrl;
    private String enlaceUrl;

    // Constructor
    public Banner(String nombre, String imagenUrl, String enlaceUrl) {
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.enlaceUrl = enlaceUrl;
    }

    public Banner() {
        
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getEnlaceUrl() {
        return enlaceUrl;
    }

    public void setEnlaceUrl(String enlaceUrl) {
        this.enlaceUrl = enlaceUrl;
    }
}
