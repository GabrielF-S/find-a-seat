package com.gabsdev.findaseat.config;

import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API de Reserva")
                        .version("1.0")
                        .description("API de rreserva e gerenciamento de lugares")
                        .contact(new Contact()
                                .name("Gabriel Ferreira dos Santos")
                                .email("gabriel.fsantos97@outlook.com")
                        )
                );
    }
}