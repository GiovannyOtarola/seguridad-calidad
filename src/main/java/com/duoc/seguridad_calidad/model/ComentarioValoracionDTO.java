package com.duoc.seguridad_calidad.model;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ComentarioValoracionDTO {
    public ComentarioValoracionDTO(long l, String string, long m) {
        //TODO Auto-generated constructor stub
    }
    public ComentarioValoracionDTO() {
        //TODO Auto-generated constructor stub
    }
    private Long id;
    private Integer valoracion;
    private String comentario;
    private boolean aprobado;
}