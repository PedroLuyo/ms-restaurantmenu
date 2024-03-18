package app.application.services.ItemCarta;

import app.domain.RestaurantMenu;
import app.infrastructure.rest.carta.webclient.CartaWebclient;
import app.infrastructure.rest.carta.dto.ItemCarta;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@NoArgsConstructor
@Slf4j
@Service
public class ItemCartaService {

    @Autowired
    private CartaWebclient cartaWebclient;

    public RestaurantMenu mapToRestaurantMenu(ItemCarta carta) {

        log.info("Mapeando ItemCarta a RestaurantMenu: {}", carta);

        RestaurantMenu restaurantMenu = new RestaurantMenu();

        // Crear un nuevo objeto PresentacionDetalle y asignarlo a restaurantMenu
        RestaurantMenu.Presentaciondetalle presentaciondetalle = new RestaurantMenu.Presentaciondetalle();

        presentaciondetalle.setId(carta.getPresentacion_detalle().getId());
        presentaciondetalle.setTipo(carta.getPresentacion_detalle().getTipo());
        presentaciondetalle.setEstado(carta.getPresentacion_detalle().getEstado());

        // Crear un nuevo objeto CategoriaDetalle y asignarlo a restaurantMenu
        RestaurantMenu.Categoriadetalle categoriadetalle = new RestaurantMenu.Categoriadetalle();

        categoriadetalle.setId(carta.getCategoria_detalle().getId());
        categoriadetalle.setNombre(carta.getCategoria_detalle().getNombre());
        categoriadetalle.setEstado(carta.getCategoria_detalle().getEstado());

        restaurantMenu.setId_menu(null);
        restaurantMenu.setId_carta(carta.getId());
        restaurantMenu.setNombre(carta.getNombre());
        restaurantMenu.setDescripcion(carta.getDescripcion());
        restaurantMenu.setPrecio(carta.getPrecio());
        restaurantMenu.setCategoria_detalle(categoriadetalle);
        restaurantMenu.setPresentacion_detalle(presentaciondetalle);
        restaurantMenu.setStock(carta.getStock());
        restaurantMenu.setEstado(carta.getEstado());
        restaurantMenu.setTipo("C");
        restaurantMenu.setMenu_detalle(null);

        return restaurantMenu;
    }


    public Flux<ItemCarta> obtenerCarta(){
       return cartaWebclient.getCarta();
    }



}
