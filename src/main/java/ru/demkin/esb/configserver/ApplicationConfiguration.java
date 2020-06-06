package ru.demkin.esb.configserver;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demkin.esb.configserver.services.BaseConfigurationUpdateStrategy;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ApplicationConfiguration {
  private static final String SEP = " / ";
  public static final String TAG_ADMIN_GROUP = "ADMIN API" + SEP + "GROUPS";
  public static final String TAG_ADMIN_META = "ADMIN API" + SEP + "META";
  public static final String TAG_ADMIN_CONFIG = "ADMIN API" + SEP + "CONFIGS";
  public static final String TAG_ADMIN_SETTINGS = "ADMIN API" + SEP + "SETTINGS";
  public static final String TAG_BUSINESS_META = "BUSINESS API" + SEP + "META";
  public static final String TAG_BUSINESS_CONFIG = "BUSINESS API" + SEP + "CONFIGS";

  private static final List<Tag> TAGS = Arrays.asList(
    new Tag().name(TAG_ADMIN_GROUP),
    new Tag().name(TAG_ADMIN_META),
    new Tag().name(TAG_ADMIN_CONFIG),
    new Tag().name(TAG_ADMIN_SETTINGS),
    new Tag().name(TAG_BUSINESS_META),
    new Tag().name(TAG_BUSINESS_CONFIG)
  );

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI() //
      .tags(TAGS)
      .components(new Components()) //
      .info(new Info() //
        .title("Сервер конфигураций") //
        .description("RESTful API для управления ползовательскими конфигурациями") //

        .version(getClass().getPackage().getImplementationVersion()));
  }

  @Bean
  public ConfigurationUpdateStrategy base() {
    return new BaseConfigurationUpdateStrategy();
  }

}
