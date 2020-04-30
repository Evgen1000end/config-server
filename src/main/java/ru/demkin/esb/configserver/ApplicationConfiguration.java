package ru.demkin.esb.configserver;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demkin.esb.configserver.services.AdminConfigurationUpdateStrategy;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;
import ru.demkin.esb.configserver.services.UserConfigurationUpdateStrategy;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI() //
      .components(new Components()) //
      .info(new Info() //
        .title("Сервер конфигураций") //
        .description("RESTful API для управления ползовательскими конфигурациями") //
        .version(getClass().getPackage().getImplementationVersion()));
  }

  @Bean
  public ConfigurationUpdateStrategy admin() {
    return new AdminConfigurationUpdateStrategy();
  }

  @Bean
  public ConfigurationUpdateStrategy user() {
    return new UserConfigurationUpdateStrategy();
  }

}
