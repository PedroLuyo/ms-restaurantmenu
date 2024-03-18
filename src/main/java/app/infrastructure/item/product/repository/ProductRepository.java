package app.infrastructure.item.product.repository;

import app.domain.RestaurantMenu;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository <RestaurantMenu,String>{

    @Query("{ 'id_carta' : ?0 }")
    Mono<RestaurantMenu> findByCartaId(int id);

    @Query("{ 'id_menu' : ?0 }")
    Mono<RestaurantMenu> findByMenuId(int id);

}
