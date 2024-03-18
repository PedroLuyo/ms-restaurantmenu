package app.application.services.ItemMenu;

import app.domain.RestaurantMenu;
import app.infrastructure.rest.menu.dto.PlatoMenuDto;
import app.infrastructure.rest.menu.webclient.MenuWebClient;
import app.infrastructure.rest.menu.dto.ItemMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class ItemMenuService {

    @Autowired
    private MenuWebClient menuWebClient;

    public RestaurantMenu mapToRestaurantMenu(PlatoMenuDto menu) {

        log.info("Mapeando ItemMenu a RestaurantMenu: {}", menu);

        RestaurantMenu restaurantMenu = new RestaurantMenu();

        // Crear un nuevo objeto MenuDetalle y asignarlo a restaurantMenu
        RestaurantMenu.MenuDetalle menuDetalle = new RestaurantMenu.MenuDetalle();



        restaurantMenu.setId_menu(menu.getIdcomida());
        restaurantMenu.setId_carta(null);
        restaurantMenu.setNombre(menu.getNombrec());
        restaurantMenu.setDescripcion(null);
        restaurantMenu.setPrecio(null);
        restaurantMenu.setCategoria(menu.getCategoria());
        restaurantMenu.setCategoria_detalle(null);
        restaurantMenu.setPresentacion_detalle(null);
        restaurantMenu.setStock(null);
        restaurantMenu.setEstado(menu.getEstado());
        restaurantMenu.setTipo("M");
        restaurantMenu.setMenu_detalle(menuDetalle);

        return restaurantMenu;
    }

    public Flux<PlatoMenuDto> obtenerMenu(){

        return menuWebClient.getMenu();
    }




}
