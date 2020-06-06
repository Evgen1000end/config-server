package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.demkin.esb.configserver.ApplicationProperties;
import ru.demkin.esb.configserver.Protocol;
import ru.demkin.esb.configserver.exception.ForbiddenException;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.model.Group;
import ru.demkin.esb.configserver.repository.UserRepository;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "API", description = "Управление конфигурациями - административный уровень")
@RequestMapping(value = "/v1/base")
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
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
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

  @Operation(summary = "Создание метаинформации конфигурации")
  @PostMapping("/config/{group}/meta")
  public void insertMeta(
    @RequestBody ConfigurationDescription value,
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group) {
    final String name = name(user, token);
    base.insert(value, name, group);
  }

  @Operation(summary = "Получение списка метаинформации для конфигураций для группы")
  @GetMapping(value = "/config/{group}/meta")
  public List<ConfigurationDescription> selectMetaByGroup(
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group) {
    final String name = name(user, token);
    return base
      .select(name)
      .stream()
      .filter(c -> c.getGroupUri().equals(group))
      .collect(Collectors.toList());
  }

  @Operation(summary = "Получение метаинформации конфигурации по URI")
  @GetMapping("/config/{group}/{uri}/meta")
  public ConfigurationDescription selectMetaByUrl(
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(user, token);
    return base.select(name, uri);
  }

  @Operation(summary = "Получение конфигурации по URI")
  @GetMapping("/config/{group}/{uri}")
  public String selectByUrl(
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(user, token);
    return base.selectConfig(name, uri);
  }

  @Operation(summary = "Редактирование конфигурации")
  @PutMapping("/config/{group}/{uri}")
  public void updateConfig
    (@RequestBody String value,
      @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
      @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
      @PathVariable(value = "group") String group,
      @PathVariable("uri") String uri) {
    final String name = name(user, token);
    base.updateConfig(value, name, uri);
  }

  @Operation(summary = "Редактирование метаинформации конфигурации")
  @PutMapping("/config/{group}/{uri}/meta")
  public void update
    (@RequestBody ConfigurationDescription value,
      @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
      @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
      @PathVariable(value = "group") String group,
      @PathVariable("uri") String uri) {
    final String name = name(user, token);
    base.update(value, name, uri);
  }

  @Operation(summary = "Удаление метаинформации конфигурации")
  @DeleteMapping("/config/{group}/{uri}/meta")
  public void delete(
    @RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @RequestHeader(value = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(user, token);
    base.delete(uri, name);
  }

  @Operation(summary = "Список пользователей")
  @GetMapping("/users")
  public List<String> findUsers() {
    return repository.findUsers();
  }

}
