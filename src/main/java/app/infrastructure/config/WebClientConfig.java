package app.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {



    @Value("${microservices.prs.platos-carta}")
    private String baseUrlCarta;

    @Value("${microservices.prs.platos-menu}")
    private String baseUrlMenu;


    @Bean
    @Primary
    public WebClient platosCartaWebClient(WebClient.Builder builder) {
        return builder.baseUrl(baseUrlCarta).build();
    }

    @Bean
    public WebClient platosWebClient(WebClient.Builder builder) {
        return builder.baseUrl(baseUrlMenu).build();
    }
}
