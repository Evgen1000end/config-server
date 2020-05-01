package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.demkin.esb.configserver.Protocol;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.repository.UserRepository;
import ru.demkin.esb.configserver.services.ConfigService;
import ru.demkin.esb.configserver.services.StrategyFactory;

import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "API", description = "Управление конфигурациями - базовый уровень")
@RequestMapping(value = "/base")
public class BaseConfigurationController {

  @Autowired
  private UserRepository repository;

  @Autowired
  private StrategyFactory factory;

  @Operation(summary = "Создание конфигурации")
  @PostMapping("/config")
  public void insert(@RequestBody ConfigurationDescription value, @RequestHeader(value = Protocol.HEADER_USER,
    required = false) String user) {
    factory.base().insert(value, user);
  }

  @Operation(summary = "Получение списка конфигураций")
  @GetMapping(value = "/config")
  public List<ConfigurationDescription> select(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user) {
    return factory.base().select(user);
  }

  @Operation(summary = "получение конфигурации по URI")
  @GetMapping("/config/{uri}")
  public ConfigurationDescription selectByUrl(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user, @PathVariable("uri") String uri) {
    return null;
  }

  @Operation(summary = "Редактирование конфига")
  @PutMapping("/config/{uri}")
  public void update(@RequestBody ConfigurationDescription value, @RequestHeader(value = Protocol.HEADER_USER,
    required = false) String user, @PathVariable("uri") String uri) {
    factory.base().update(value, uri);
  }

  @Operation(summary = "Удаление конфига")
  @DeleteMapping("/config/{uri}")
  public void delete(@RequestHeader(value = Protocol.HEADER_USER, required = false) String user,
    @PathVariable("uri") String uri) {
    factory.base().delete(uri);
  }

  @Operation(summary = "Список пользователей")
  @GetMapping("/users")
  public List<String> findUsers() {
    return repository.findUsers();
  }

}
