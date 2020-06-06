package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.demkin.esb.configserver.Protocol;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;

@RestController
@Tag(name = "BUSINESS", description = "Управление конфигурациями - бизнес уровень")
public class ServiceConfigurationController {

  @Autowired
  private ConfigurationUpdateStrategy base;

  @Operation(summary = "Создание конфигурации")
  @PostMapping("/config")
  public void insert(@RequestBody ConfigurationDescription value, @RequestHeader(value = Protocol.HEADER_USER,
    required = false) String user) {
    if (isAdmin(user)) {
      base.insert(value, user);
    } else {
      throw new IllegalArgumentException("Unsupported operation. User " + user + " can't create config.");
    }
  }

  @Operation(summary = "Получение конфигурации по URI")
  @GetMapping("/config/{uri}")
  public ConfigurationDescription selectByUrl(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user, @PathVariable("uri") String uri) {
    if (isAdmin(user)) {
      return base.selectForAdmin(uri);
    } else {
      return base.selectForUser(user, uri);
    }
  }

  @Operation(summary = "Редактирование конфига")
  @PutMapping("/config/{uri}")
  public void update(@RequestBody ConfigurationDescription value, @RequestHeader(value = Protocol.HEADER_USER,
    required = false) String user, @PathVariable("uri") String uri) {
    if (isAdmin(user)) {
      base.updateForAdmin(value, uri);
    } else {
      base.update(value, user, uri);
    }
  }

  @Operation(summary = "Удаление конфига")
  @DeleteMapping("/config/{uri}")
  public void delete(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @PathVariable("uri") String uri) {
    if (isAdmin(user)) {
      base.deleteForAdmin(uri);
    } else {
      base.delete(uri, user);
    }
  }

  //TODO - group!!

  private boolean isAdmin(String username) {
    return username == null;
  }

}
