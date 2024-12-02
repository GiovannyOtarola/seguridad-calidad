package com.duoc.seguridad_calidad.model;




import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Receta {

    public Receta(int i, String string, String string2, String string3, String string4, String string5, String string6,
            int j, String string7) {
        //TODO Auto-generated constructor stub
    }
    public Receta() {
        //TODO Auto-generated constructor stub
    }
    private Long id;
    private String nombre;
    private String tipoCocina;
    private String paisOrigen;
    private String dificultad;
    private String ingredientes;
    private String instrucciones;
    private int tiempoCoccion;
    private String fotografiaUrl;
    private String urlVideo;

}

