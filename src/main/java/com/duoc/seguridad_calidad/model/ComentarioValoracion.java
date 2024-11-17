package com.duoc.seguridad_calidad.model;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ComentarioValoracion  {
    private Long id;
    private Long recetaId;
    private Integer valoracion;
    private String comentario;
}
