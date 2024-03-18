package app.infrastructure.rest.carta.repository;

import app.infrastructure.rest.carta.dto.ItemCarta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemCartaRepository extends ReactiveCrudRepository<ItemCarta, Integer> {
}
