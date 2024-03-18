package app.infrastructure.rest.carta.webclient;

import app.infrastructure.rest.carta.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Component
@Service
public class CartaWebclient {

    private final WebClient webClient;

    @Autowired
    public CartaWebclient(@Qualifier("platosCartaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<ItemCarta> getCarta() {
        return webClient.get()
                .uri("/obtener")  // Actualiza la URI según tu lógica
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ItemCarta.class);
    }
}


