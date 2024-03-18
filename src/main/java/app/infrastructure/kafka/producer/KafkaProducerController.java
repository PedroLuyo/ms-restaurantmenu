package app.infrastructure.kafka.producer;

import app.application.controller.ItemCarta.ItemCartaController;
import app.application.controller.ItemMenu.ItemMenuController;
import app.application.services.ItemCarta.ItemCartaService;
import app.application.services.ItemMenu.ItemMenuService;
import app.infrastructure.rest.carta.dto.ItemCarta;
import app.infrastructure.rest.carta.dto.PlatoCartaDto;
import app.infrastructure.rest.menu.dto.PlatoMenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/prs/")
public class KafkaProducerController {

    private final KafkaTemplate<String, PlatoCartaDto> kafkaTemplateCarta;
    private final KafkaTemplate<String, PlatoMenuDto> kafkaTemplateMenu;

    @Autowired
    public KafkaProducerController(KafkaTemplate<String, PlatoCartaDto> kafkaTemplateCarta,
                                   KafkaTemplate<String, PlatoMenuDto> kafkaTemplateMenu) {
        this.kafkaTemplateCarta = kafkaTemplateCarta;
        this.kafkaTemplateMenu = kafkaTemplateMenu;
    }

    @Autowired
    private ItemCartaController itemCartaController;

    @Autowired
    private ItemCartaService itemCartaService;

    @Autowired
    private ItemMenuController itemMenuController;

    @Autowired
    private ItemMenuService itemMenuService;

    @GetMapping("/plato-menu/obtener")
    public Flux<PlatoMenuDto> obtenerPlatosMenu(){
        return itemMenuService.obtenerMenu();
    }

    @GetMapping("/plato-carta/obtener")
    public Flux<ItemCarta> obtenerPlatosCarta(){
        return itemCartaService.obtenerCarta();
    }

    @PostMapping("/plato-carta/crear")
    public String crearPlatoCarta(@RequestBody PlatoCartaDto plato) {
        try {
            PlatoCartaDto platokafka = new PlatoCartaDto();
            platokafka.setNombre(plato.getNombre());
            platokafka.setDescripcion(plato.getDescripcion());
            platokafka.setPrecio(plato.getPrecio());
            platokafka.setId_categoria(plato.getId_categoria());
            platokafka.setId_presentacion(plato.getId_presentacion());
            platokafka.setStock(plato.getStock());

            kafkaTemplateCarta.send("pcmscreate", platokafka);

            return "Plato de carta creado exitosamente.";
        } catch (Exception e) {
            System.err.println("Error al procesar el objeto PlatoCartaDto para crear: " + e.getMessage());
            return "Error al crear el plato de carta.";
        }
    }

    @PostMapping("/plato-menu/crear")
    public String crearPlatoMenu(@RequestBody PlatoMenuDto plato) {
        try {
            PlatoMenuDto platoMenuDto = new PlatoMenuDto();
            platoMenuDto.setNombrec(plato.getNombrec());
            platoMenuDto.setCategoria(plato.getCategoria());


            kafkaTemplateMenu.send("pmmscreate", platoMenuDto);

            return "Plato de menu creado exitosamente.";
        } catch (Exception e) {
            System.err.println("Error al procesar el objeto PlatoMenuDto para crear: " + e.getMessage());
            return "Error al crear el plato de carta.";
        }
    }

    @PostMapping("/plato-carta/editar/{id}")
    public String editarPlatoCarta(@PathVariable Integer id, @RequestBody PlatoCartaDto plato) {
        try {
            plato.setId(id);
            kafkaTemplateCarta.send("pcmsedit", plato);
            return "Plato de carta editado exitosamente.";
        } catch (Exception e) {
            System.err.println("Error al procesar el objeto PlatoCartaDto para editar: " + e.getMessage());
            return "Error al editar el plato de carta.";
        }
    }


    @PostMapping("/plato-carta/desactivar/{id}")
    public String desactivarPlatoCarta(@PathVariable Integer id) {
        try {
            PlatoCartaDto plato = new PlatoCartaDto();
            plato.setId(id);
            kafkaTemplateCarta.send("pcmsdesactivate", plato);
            return "Plato de carta desactivado exitosamente.";
        } catch (Exception e) {
            System.err.println("Error al procesar el id para desactivar: " + e.getMessage());
            return "Error al desactivar el plato de carta.";
        }
    }

    @PostMapping("/plato-carta/restaurar/{id}")
    public String restaurarPlatoCarta(@PathVariable Integer id) {
        try {
            PlatoCartaDto plato = new PlatoCartaDto();
            plato.setId(id);
            kafkaTemplateCarta.send("pcmsrestore", plato);
            return "Plato de carta restaurado exitosamente.";
        } catch (Exception e) {
            System.err.println("Error al procesar el id para restaurar: " + e.getMessage());
            return "Error al restaurar el plato de carta.";
        }
    }
}

