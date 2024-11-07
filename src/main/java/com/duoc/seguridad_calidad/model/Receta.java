package com.duoc.seguridad_calidad.model;




import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Receta {

    private String nombre;
    private String tipoCocina;
    private String paisOrigen;
    private String dificultad;
    private String ingredientes;
    private String instrucciones;
    private int tiempoCoccion;
    private String fotografiaUrl;

}

