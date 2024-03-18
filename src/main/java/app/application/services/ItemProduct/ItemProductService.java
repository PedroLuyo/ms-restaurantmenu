package app.application.services.ItemProduct;

import app.application.services.ItemCarta.ItemCartaService;
import app.application.services.ItemMenu.ItemMenuService;
import app.domain.RestaurantMenu;
import app.infrastructure.item.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ItemProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemMenuService itemMenuService;

    @Autowired
    private ItemCartaService itemCartaService;

    public Mono<Void> saveDataToMongo() {
        // Obtener datos de ItemCartaService y mapearlos a RestaurantMenu
        Flux<RestaurantMenu> cartaFlux = itemCartaService.obtenerCarta().map(itemCartaService::mapToRestaurantMenu);

        // Obtener datos de ItemMenuService y mapearlos a RestaurantMenu
        Flux<RestaurantMenu> menuFlux = itemMenuService.obtenerMenu().map(itemMenuService::mapToRestaurantMenu);

        // Combinar ambos Flux y guardar en la base de datos
        return Flux.concat(cartaFlux, menuFlux)
                .flatMap(productRepository::save)
                .then();
    }




    public Mono<Void> guardarPlatosCarta() {
        // Obtener datos de ItemCartaService y mapearlos a RestaurantMenu
        Flux<RestaurantMenu> cartaFlux = itemCartaService.obtenerCarta().map(itemCartaService::mapToRestaurantMenu);

        return Flux.concat(cartaFlux)
                .flatMap(productRepository::save)
                .then();
    }

    public Mono<Void> guardarPlatosMenu() {
        // Obtener datos de ItemMenuService y mapearlos a RestaurantMenu
        Flux<RestaurantMenu> menuFlux = itemMenuService.obtenerMenu().map(itemMenuService::mapToRestaurantMenu);

        return Flux.concat(menuFlux)
                .flatMap(productRepository::save)
                .then();
    }
}