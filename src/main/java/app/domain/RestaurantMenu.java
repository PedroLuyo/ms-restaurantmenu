package app.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@Document(collection = "products", language = "javascript")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantMenu {

    @Id
    private String id;
    private Integer id_carta;
    private Integer id_menu;
    private String nombre;
    private String descripcion;
    private String categoria;
    private Double precio;
    private Integer stock;
    private String tipo; // "C" o "M"

        private Categoriadetalle categoria_detalle;

        private Presentaciondetalle presentacion_detalle;

        private MenuDetalle menu_detalle;
    private String estado;

    @Data
    @Getter @Setter
    public static class MenuDetalle {
        private Integer id;
        private String nombre;
        private String precio;
    }

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
