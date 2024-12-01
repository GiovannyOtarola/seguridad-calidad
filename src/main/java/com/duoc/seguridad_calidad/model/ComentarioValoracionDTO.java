package com.duoc.seguridad_calidad.model;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ComentarioValoracionDTO {
    private Long id;
    private Integer valoracion;
    private String comentario;
    private boolean aprobado;
}
