package ru.demkin.esb.configserver;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI() //
                .components(new Components()) //
                .info(new Info() //
                        .title("Адаптер УФЭС") //
                        .description("RESTful API для отправки в ESB УФЭС сообщений") //
                        .version(getClass().getPackage().getImplementationVersion()));
    }
}
