package app.infrastructure.rest.carta.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoCartaDto {

    private Integer id;

    private String nombre;

    private String descripcion;

    private double precio;

    private int id_categoria ;

    private int id_presentacion;

    private int stock;

}

