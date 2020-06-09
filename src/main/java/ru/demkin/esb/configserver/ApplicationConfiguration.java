package ru.demkin.esb.configserver;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demkin.esb.configserver.services.BaseConfigurationUpdateStrategy;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;

import java.util.Arrays;
import java.util.Collections;
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
  public static final String TAG_AUTH = "BUSINESS API" + SEP + "AUTHENTICATION";

  private static final List<Tag> TAGS = Arrays.asList(
    new Tag().name(TAG_ADMIN_GROUP),
    new Tag().name(TAG_ADMIN_META),
    new Tag().name(TAG_ADMIN_CONFIG),
    new Tag().name(TAG_ADMIN_SETTINGS),
    new Tag().name(TAG_BUSINESS_META),
    new Tag().name(TAG_BUSINESS_CONFIG),
    new Tag().name(TAG_AUTH)
  );

  private static final String API_KEY = "apiKey";


  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI() //
//      .components(new Components()
//        .addSecuritySchemes("bearer-key",
//          new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
//      .components(new Components()
//        .addSecuritySchemes(API_KEY,apiKeySecuritySchema()))
//      .security(Collections.singletonList(new SecurityRequirement().addList(API_KEY)))
//      .components(new Components().addSecuritySchemes("bearer-key",
//        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
     // .security(Collections.singletonList(new ApiKey(RestSecurityFilter.HTTP_REQUEST_API_KEY, RestSecurityFilter.HTTP_REQUEST_API_KEY, "header")))
      .tags(TAGS)
      .components(new Components()) //
      .info(new Info() //
        .title("Сервер конфигураций") //
        .description("RESTful API для управления ползовательскими конфигурациями") //

        .version(getClass().getPackage().getImplementationVersion()));
  }


  public SecurityScheme apiKeySecuritySchema() {
    return new SecurityScheme()
      .name(Protocol.HEADER_ADMIN) // authorisation-token
      .description("Description about the TOKEN")
      .in(SecurityScheme.In.HEADER)
      .type(SecurityScheme.Type.APIKEY);
  }


    @Bean
  public ConfigurationUpdateStrategy base() {
    return new BaseConfigurationUpdateStrategy();
  }

}
