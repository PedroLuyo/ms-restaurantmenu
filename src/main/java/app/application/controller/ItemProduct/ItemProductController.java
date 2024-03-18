package app.application.controller.ItemProduct;

import app.application.services.ItemProduct.ItemProductService;
import app.domain.RestaurantMenu;
import app.infrastructure.item.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/products/")
public class ItemProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemProductService itemProductService;

    @GetMapping("/obtener")
    private Flux<RestaurantMenu> obtenerProduct() {
        return productRepository.findAll();
    }

    @PostMapping("/crear")
    private Mono<RestaurantMenu> crearProduct(@RequestBody RestaurantMenu restaurantMenu) {
        return productRepository.save(restaurantMenu);
    }

    @PostMapping("/guardar")
    private Mono<String> guardarDataToMongo() {
        return itemProductService.saveDataToMongo()
                .thenReturn("Productos guardados exitosamente");
    }
}
