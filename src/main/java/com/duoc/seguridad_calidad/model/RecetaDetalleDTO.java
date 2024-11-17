package com.duoc.seguridad_calidad.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RecetaDetalleDTO {
    private Receta receta;
    private List<ComentarioValoracionView> comentarios;
}
