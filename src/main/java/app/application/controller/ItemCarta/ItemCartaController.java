package app.application.controller.ItemCarta;

import app.application.services.ItemCarta.ItemCartaService;
import app.infrastructure.rest.carta.webclient.CartaWebclient;
import app.infrastructure.rest.carta.dto.ItemCarta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/platos-carta")
public class ItemCartaController {

    @Autowired
    private CartaWebclient cartaWebclient;

    @Autowired
    private ItemCartaService itemCartaService;


    @GetMapping("/obtener")
    public Flux<ItemCarta> obtenerPlatosCarta(){
        return itemCartaService.obtenerCarta();
    }
}
