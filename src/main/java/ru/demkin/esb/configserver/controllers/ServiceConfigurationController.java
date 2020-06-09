package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.demkin.esb.configserver.ApplicationConfiguration;
import ru.demkin.esb.configserver.ApplicationProperties;
import ru.demkin.esb.configserver.Protocol;
import ru.demkin.esb.configserver.exception.ForbiddenException;
import ru.demkin.esb.configserver.model.ConfigurationMetaRequest;
import ru.demkin.esb.configserver.model.ConfigurationMetaResponse;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;

@RestController
//@Tag(name = "BUSINESS", description = "Управление конфигурациями - бизнес уровень")
@RequestMapping("/v1")
public class ServiceConfigurationController {

  @Autowired
  private ConfigurationUpdateStrategy base;

  @Autowired
  private AuthController authController;

  @Autowired
  private ApplicationProperties properties;

  private String name(String user, String token) {
    final boolean tokenEmpty = StringUtils.isBlank(token);
    final boolean userEmpty = StringUtils.isBlank(user);
    if (tokenEmpty) {
      if (userEmpty) {
        if (properties.isAuthEnabled()) {
          throw new ForbiddenException("Access denied. Empty token for ADMIN.");
        } else {
          return null;
        }
      } else {
        return user;
      }
    } else {
      if (tokenIsValid(token)) {
        return null;
      } else {
        throw new ForbiddenException("Token " + token + " invalid");
      }
    }
  }

  private boolean tokenIsValid(String token) {
    return authController.sessionValid(token);
  }

  private void validateConfigurationMetaRequest(ConfigurationMetaRequest request) {
    if (StringUtils.isBlank(request.getLabel())) {
      throw new IllegalArgumentException("label should not be blank");
    }
    if (StringUtils.isBlank(request.getUri())) {
      throw new IllegalArgumentException("uri should not be blank");
    }
  }

  @Operation(summary = "Создание мета конфигурации", tags = ApplicationConfiguration.TAG_BUSINESS_META)
  @PostMapping("/config/{group}/meta")
  public void insertMeta(
    @RequestBody ConfigurationMetaRequest value,
    @RequestHeader(name = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group) {
    final String name = name(user, token);
    if (isAdmin(name)) {
      validateConfigurationMetaRequest(value);
      base.insert(value, name, group);
    } else {
      throw new IllegalArgumentException("Unsupported operation. User " + user + " can't create meta config.");
    }
  }

  @Operation(summary = "Получение мета конфигурации по URI", tags = ApplicationConfiguration.TAG_BUSINESS_META)
  @GetMapping("/config/{group}/{uri}/meta")
  public ConfigurationMetaResponse selectMetaByUrl(
    @RequestHeader(name = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(user, token);
    if (isAdmin(name)) {
      return base.selectForAdmin(uri);
    } else {
      return base.selectForUser(name, uri);
    }
  }

  @Operation(summary = "Редактирование мета конфигурации конфига", tags = ApplicationConfiguration.TAG_BUSINESS_META)
  @PutMapping("/config/{group}/{uri}/meta")
  public void updateMeta(@RequestBody ConfigurationMetaRequest value, @RequestHeader(name = Protocol.HEADER_USER,
    required = false) String user, @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable("uri") String uri,
    @PathVariable(value = "group") String group) {
    final String name = name(user, token);
    if (isAdmin(name)) {
      validateConfigurationMetaRequest(value);
      base.updateForAdmin(value, uri);
    } else {
      //
      // base.update(value, user, uri);
      throw new IllegalArgumentException("Unsupported operation. User " + user + " can't update meta config.");
    }
  }

  @Operation(summary = "Удаление мета конфигурации конфига", tags = ApplicationConfiguration.TAG_BUSINESS_META)
  @DeleteMapping("/config/{group}/{uri}/meta")
  public void deleteMeta(@RequestHeader(name = Protocol.HEADER_USER, required = false) String user,
    @PathVariable("uri") String uri, @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group) {
    final String name = name(user, token);

    if (isAdmin(name)) {
      base.deleteForAdmin(uri);
    } else {
      throw new IllegalArgumentException("Unsupported operation. User " + user + " can't delete meta config.");
      //  base.delete(uri, user);
    }
  }

  @Operation(summary = "Получение конфигурации по URI", tags = ApplicationConfiguration.TAG_BUSINESS_CONFIG)
  @GetMapping("/config/{group}/{uri}")
  public String selectByUrl(
    @RequestHeader(name = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(user, token);
    if (isAdmin(name)) {
      return base.selectConfigForAdmin(uri);
    } else {
      return base.selectConfigForUser(name, uri);
    }
  }

  @Operation(summary = "Редактирование конфигурации", tags = ApplicationConfiguration.TAG_BUSINESS_CONFIG)
  @PutMapping("/config/{group}/{uri}")
  public void updateConfig
    (@RequestBody String value,
      @RequestHeader(name = Protocol.HEADER_USER, required = false) String user,
      @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
      @PathVariable(value = "group") String group,
      @PathVariable("uri") String uri) {
    final String name = name(user, token);
    if (isAdmin(name)) {
      base.updateConfigForAdmin(value, uri);
    } else {
      base.updateConfigForUser(name, value, uri);
    }
  }

  private boolean isAdmin(String username) {
    return username == null;
  }

}
