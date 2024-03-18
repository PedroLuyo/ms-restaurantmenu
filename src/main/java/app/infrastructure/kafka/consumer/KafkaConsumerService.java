package app.infrastructure.kafka.consumer;

import app.application.services.ItemCarta.ItemCartaService;
import app.application.services.ItemMenu.ItemMenuService;
import app.domain.RestaurantMenu;
import app.infrastructure.item.product.repository.ProductRepository;
import app.infrastructure.rest.carta.dto.ItemCarta;
import app.infrastructure.rest.menu.dto.ItemMenu;
import app.infrastructure.rest.menu.dto.PlatoMenuDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class KafkaConsumerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemCartaService itemCartaService;

    @Autowired
    private ItemMenuService itemMenuService;

    @KafkaListener(topics = "crmscreate", groupId = "my-consumer-group")
    public void consumeMessage(String json) {
        try {
            if (json.contains("presentacion_detalle")) {
                mapAndSaveCarta(json);
            } else if (json.contains("menu_detalle")) {
                mapAndSaveMenu(json);
            } else {
                System.err.println("Guardar plato: Tipo de plato desconocido para el JSON: " + json);
            }
        } catch (Exception e) {
            System.err.println("Guardar plato: Error al procesar el mensaje JSON: " + json);
            e.printStackTrace();
        }
    }

    // Métodos auxiliares
    private <T> T mapFromJson(String json, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            System.err.println("Error al mapear JSON a objeto: " + e.getMessage());
            return null;
        }
    }

    private void mapAndSaveCarta(String json) {
        ItemCarta itemCarta = mapFromJson(json, ItemCarta.class);
        RestaurantMenu restaurantMenu = itemCartaService.mapToRestaurantMenu(itemCarta);
        restaurantMenu.setTipo("C");

        String nombrePlato = restaurantMenu.getNombre(); // Obtener el nombre del plato

        saveToMongo(restaurantMenu)
                .subscribe(savedMenu -> {
                    // Realiza cualquier acción necesaria después del guardado
                    System.out.println("Plato Carta '" + nombrePlato + "' guardado con éxito: " + savedMenu);
                }, error -> {
                    System.err.println("Error al guardar el Plato Carta: " + error.getMessage());
                    error.printStackTrace();
                });
    }

    private void mapAndSaveMenu(String json) {
        PlatoMenuDto platoMenuDto = mapFromJson(json, PlatoMenuDto.class);
        RestaurantMenu restaurantMenu = itemMenuService.mapToRestaurantMenu(platoMenuDto);
        restaurantMenu.setTipo("M");

        String nombrePlato = restaurantMenu.getNombre(); // Obtener el nombre del plato

        saveToMongo(restaurantMenu)
                .subscribe(savedMenu -> {
                    // Realiza cualquier acción necesaria después del guardado
                    System.out.println("Plato Menú '" + nombrePlato + "' guardado con éxito: " + savedMenu);
                }, error -> {
                    System.err.println("Error al guardar el Plato Menú: " + error.getMessage());
                    error.printStackTrace();
                });
    }



    private Mono<RestaurantMenu> saveToMongo(RestaurantMenu restaurantMenu) {
        return productRepository.save(restaurantMenu)
                .doOnSuccess(savedMenu -> {
                    System.out.println("Guardado exitoso en MongoDB");
                })
                .doOnError(error -> {
                    System.err.println("Error al guardar en MongoDB: " + error.getMessage());
                    error.printStackTrace();
                });
    }





    private static final List<ComparacionCampo> camposCompararCarta = List.of(
            new ComparacionCampo("nombre", "getNombre"),
            new ComparacionCampo("descripcion", "getDescripcion"),
            new ComparacionCampo("precio", "getPrecio"),
            new ComparacionCampo("stock", "getStock"),
            new ComparacionCampo("estado", "getEstado")


            // Agrega más campos según sea necesario
    );

    private static final List<ComparacionCampo> camposCompararMenu = List.of(
            new ComparacionCampo("nombre", "getNombre"),
            new ComparacionCampo("categoria", "getCategoria"),
            new ComparacionCampo("estado", "getEstado")
            // Agrega más campos según sea necesario
    );

    @KafkaListener(topics = "crmsedit", groupId = "my-consumer-group")
    public void consumeEditMessage(String json) {
        try {
            if (json.contains("presentacion_detalle")) {
                editPlato(json, camposCompararCarta, "C");
            } else if (json.contains("menu_detalle")) {
                editPlato(json, camposCompararMenu, "M");
            } else {
                System.err.println("Editar plato: Tipo de plato desconocido para el JSON: " + json);
            }
        } catch (Exception e) {
            System.err.println("Editar plato: Error al procesar el mensaje JSON: " + json);
            e.printStackTrace();
        }
    }

    private <T> void editPlato(String json, List<ComparacionCampo> camposComparar, String tipo) {
        if ("C".equals(tipo)) {
            ItemCarta itemCarta = mapFromJson(json, ItemCarta.class);
            editAndSaveToMongo(itemCarta, tipo, camposComparar);
        } else if ("M".equals(tipo)) {
            ItemMenu itemMenu = mapFromJson(json, ItemMenu.class);
            editAndSaveToMongo(itemMenu, tipo, camposComparar);
        } else {
            System.err.println("Editar plato: Tipo de plato desconocido: " + tipo);
        }
    }

    private <T> void editAndSaveToMongo(T item, String tipo, List<ComparacionCampo> camposComparar) {
        // Obtener el id del plato
        int itemId;
        if (item instanceof ItemCarta) {
            itemId = ((ItemCarta) item).getId();
        } else if (item instanceof ItemMenu) {
            itemId = ((ItemMenu) item).getId();
        } else {
            throw new IllegalArgumentException("Tipo de objeto desconocido: " + item.getClass().getName());
        }

        // Obtener el plato existente de MongoDB
        Mono<RestaurantMenu> existingMenuMono;
        if ("C".equals(tipo)) {
            existingMenuMono = productRepository.findByCartaId(itemId);
        } else if ("M".equals(tipo)) {
            existingMenuMono = productRepository.findByMenuId(itemId);
        } else {
            throw new IllegalArgumentException("Tipo de plato desconocido: " + tipo);
        }

        existingMenuMono.subscribe(existingMenu -> {
            // Comparar campos y actualizar si es necesario
            boolean updated = compareAndUpdateFields(item, existingMenu, camposComparar);

            if (updated) {
                saveToMongo(existingMenu)
                        .subscribe(savedMenu -> {
                            // Realiza cualquier acción necesaria después de la actualización
                            System.out.println("Plato actualizado con éxito: " + savedMenu);
                        }, error -> {
                            System.err.println("Error al actualizar el plato: " + error.getMessage());
                            error.printStackTrace();
                        });
            } else {
                System.out.println("No hay cambios en el plato, no se requiere actualización.");
            }
        });
    }

    private <T> boolean compareAndUpdateFields(T newItem, RestaurantMenu existingMenu, List<ComparacionCampo> camposComparar) {
        boolean updated = false;

        for (ComparacionCampo campo : camposComparar) {
            String nombreCampo = campo.getNombreCampo();
            String metodoObtenerCampo = campo.getMetodoObtenerCampo();

            try {
                // Obtener valor del nuevo objeto
                Object valorNuevo = newItem.getClass().getMethod(metodoObtenerCampo).invoke(newItem);

                // Obtener valor del objeto existente
                Object valorExistente = existingMenu.getClass().getMethod(metodoObtenerCampo).invoke(existingMenu);

                // Comparar y actualizar si es necesario
                if (valorNuevo != null && !valorNuevo.equals(valorExistente)) {
                    existingMenu.getClass().getMethod("set" + nombreCampo.substring(0, 1).toUpperCase() + nombreCampo.substring(1), valorNuevo.getClass()).invoke(existingMenu, valorNuevo);
                    updated = true;
                }
            } catch (Exception e) {
                System.err.println("Error al comparar y actualizar campo: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return updated;
    }

    private static class ComparacionCampo {
        private final String nombreCampo;
        private final String metodoObtenerCampo;

        public ComparacionCampo(String nombreCampo, String metodoObtenerCampo) {
            this.nombreCampo = nombreCampo;
            this.metodoObtenerCampo = metodoObtenerCampo;
        }

        public String getNombreCampo() {
            return nombreCampo;
        }

        public String getMetodoObtenerCampo() {
            return metodoObtenerCampo;
        }
    }
}
