package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.demkin.esb.configserver.ApplicationProperties;
import ru.demkin.esb.configserver.Protocol;
import ru.demkin.esb.configserver.exception.ForbiddenException;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.model.Group;
import ru.demkin.esb.configserver.repository.UserRepository;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;

import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "API", description = "Управление конфигурациями - административный уровень")
@RequestMapping(value = "/base")
public class BaseConfigurationController {

  @Autowired
  private UserRepository repository;

  @Autowired
  private ConfigurationUpdateStrategy base;

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
    return true;
  }


  @Operation(summary = "Создание группы")
  @PostMapping("/group")
  public void insertGroup(
    @RequestBody Group value,
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token) {
    final String name = name(user, token);
    if (name != null) {
      throw new ForbiddenException("User " + name + " doesn't have permission to create group");
    } else {
      base.insertGroup(value);
    }
  }

  @Operation(summary = "Получение списка групп")
  @GetMapping(value = "/group")
  public List<Group> selectGroups(
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token) {
    name(user, token);
    return base.findAllGroups();
  }

  @Operation(summary = "Получение группы по URI")
  @GetMapping("/group/{uri}")
  public Group selectGroupByUrl(
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable("uri") String uri) {
    name(user, token);
    return base.findGroupByUri(uri);
  }

  @Operation(summary = "Редактирование группы")
  @PutMapping("/group/{uri}")
  public void updateGroup(
    @RequestBody Group value,
    @RequestHeader(value = Protocol.HEADER_USER,  required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable("uri") String uri) {
    final String name = name(user, token);
    if (name != null) {
      throw new ForbiddenException("User " + name + " doesn't have permission to update group");
    } else {
      base.updateGroup(value, uri);
    }
  }

  @Operation(summary = "Удаление группы")
  @DeleteMapping("/group/{uri}")
  public void deleteGroup(
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable("uri") String uri) {
    final String name = name(user, token);
    if (name != null) {
      throw new ForbiddenException("User " + name + " doesn't have permission to delete group");
    } else {
      base.deleteGroup(uri);
    }
  }


  //TODO - группы и токены
  @Operation(summary = "Создание конфигурации")
  @PostMapping("/config")
  public void insert(
    @RequestBody ConfigurationDescription value,
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user) {
    base.insert(value, user);
  }

  //TODO - группы и токены
  @Operation(summary = "Получение списка конфигураций")
  @GetMapping(value = "/config")
  public List<ConfigurationDescription> select(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user) {
    return base.select(user);
  }

  //TODO - группы и токены
  @Operation(summary = "Gолучение конфигурации по URI")
  @GetMapping("/config/{uri}")
  public ConfigurationDescription selectByUrl(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user, @PathVariable("uri") String uri) {
    return base.select(user, uri);
  }

  //TODO - группы и токены
  @Operation(summary = "Редактирование конфига")
  @PutMapping("/config/{uri}")
  public void update(@RequestBody ConfigurationDescription value, @RequestHeader(value = Protocol.HEADER_USER,
    required = false) String user, @PathVariable("uri") String uri) {
    base.update(value, user, uri);
  }


  //TODO - группы и токены
  @Operation(summary = "Удаление конфига")
  @DeleteMapping("/config/{uri}")
  public void delete(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @PathVariable("uri") String uri) {
    base.delete(uri, user);
  }

  //TODO - группы и токены
  @Operation(summary = "Список пользователей")
  @GetMapping("/users")
  public List<String> findUsers() {
    return repository.findUsers();
  }

  // TODO - разделить мета и не мета

}
