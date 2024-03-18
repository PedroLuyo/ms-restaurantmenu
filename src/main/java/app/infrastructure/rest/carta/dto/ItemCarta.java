package app.infrastructure.rest.carta.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
public class ItemCarta {

    @Id
    private int id;

    private String nombre;

    private String descripcion;

    private Double precio;

    private Categoriadetalle categoria_detalle;

    private Presentaciondetalle presentacion_detalle;

    private int stock;

    private String estado;

    @Data
    @Getter @Setter
    public static class Categoriadetalle{

        private int id;
        private String nombre;
        private String estado;

    }

    @Data
    @Getter @Setter
    public static class Presentaciondetalle{

        private int id;
        private String tipo;
        private String estado;


    }

}

