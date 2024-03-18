package app.infrastructure.rest.menu.webclient;


import app.infrastructure.rest.menu.dto.PlatoMenuDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import app.infrastructure.rest.menu.dto.ItemMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


@Service
public class MenuWebClient {

    @Autowired
    @Qualifier("platosWebClient")
    private WebClient webClient;

    public Flux<PlatoMenuDto> getMenu() {
        return webClient.get()
                .uri("/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(PlatoMenuDto.class);
    }
}

