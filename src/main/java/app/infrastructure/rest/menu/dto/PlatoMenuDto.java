package app.infrastructure.rest.menu.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class PlatoMenuDto {

    @Id
    private Integer idcomida;

    private String nombrec;

    private String categoria;

    private String estado;
}
