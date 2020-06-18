package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import ru.demkin.esb.configserver.model.BaseConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.ConfigurationMetaRequest;
import ru.demkin.esb.configserver.model.ConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.GroupRequest;
import ru.demkin.esb.configserver.model.GroupResponse;
import ru.demkin.esb.configserver.repository.UserRepository;
import ru.demkin.esb.configserver.services.ConfigurationUpdateStrategy;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1/base")
public class BaseConfigurationController {

  @Autowired
  private UserRepository repository;

  @Autowired
  private ConfigurationUpdateStrategy base;

  @Autowired
  private AuthController authController;

  private String name(String token) {
    if (!authController.isLogged()) {
      throw new ForbiddenException("Access denied. You must login first.");
    }
    final boolean tokenEmpty = StringUtils.isBlank(token);
    if (tokenEmpty) {
      throw new ForbiddenException("Access denied. Empty token for ADMIN.");
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

  private void validateGroupRequest(GroupRequest groupRequest) {
    if (StringUtils.isBlank(groupRequest.getLabel())) {
      throw new IllegalArgumentException("label should not be blank");
    }
    if (StringUtils.isBlank(groupRequest.getUri())) {
      throw new IllegalArgumentException("uri should not be blank");
    }
  }

  private void validateConfigurationMetaRequest(ConfigurationMetaRequest request) {
    if (StringUtils.isBlank(request.getLabel())) {
      throw new IllegalArgumentException("label should not be blank");
    }
    if (StringUtils.isBlank(request.getUri())) {
      throw new IllegalArgumentException("uri should not be blank");
    }
  }

  @Operation(summary = "Создание группы", tags = ApplicationConfiguration.TAG_ADMIN_GROUP)
  @PostMapping("/group")
  public GroupResponse insertGroup(

    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @RequestBody GroupRequest value) {
    name(token);
    validateGroupRequest(value);
    base.insertGroup(value);
    return base.findGroupByUri(value.getUri());

  }

  @Operation(summary = "Получение списка групп", tags = ApplicationConfiguration.TAG_ADMIN_GROUP)
  @GetMapping(value = "/group")
  public List<GroupResponse> selectGroups(@RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token) {
    name(token);
    return base.findAllGroups();
  }

  @Operation(summary = "Получение группы по URI", tags = ApplicationConfiguration.TAG_ADMIN_GROUP)
  @GetMapping("/group/{uri}")
  public GroupResponse selectGroupByUrl(
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable("uri") String uri) {
    name(token);
    return base.findGroupByUri(uri);
  }

  @Operation(summary = "Редактирование группы", tags = ApplicationConfiguration.TAG_ADMIN_GROUP)
  @PutMapping("/group/{uri}")
  public void updateGroup(
    @RequestBody GroupRequest value,
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable("uri") String uri) {
    final String name = name(token);
    validateGroupRequest(value);
    base.updateGroup(value, uri);
  }

  @Operation(summary = "Удаление группы", tags = ApplicationConfiguration.TAG_ADMIN_GROUP)
  @DeleteMapping("/group/{uri}")
  public void deleteGroup(
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable("uri") String uri) {
    name(token);
    List<BaseConfigurationMetaResponse> configurationDescriptions = selectMetaByGroup(token, uri);
    if (configurationDescriptions.isEmpty()) {
      base.deleteGroup(uri);
    } else {
      throw new IllegalArgumentException("Group " + uri + " contain at least one config");
    }

  }

  @Operation(summary = "Создание метаинформации конфигурации", tags = ApplicationConfiguration.TAG_ADMIN_META)
  @PostMapping("/config/{group}/meta")
  public ConfigurationMetaResponse insertMeta(
    @RequestBody ConfigurationMetaRequest value,
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group) {
    final String name = name(token);
    validateConfigurationMetaRequest(value);
    base.insert(value, name, group);
    return base.select(name, value.getUri());
  }

  @Operation(summary = "Получение списка метаинформации для конфигураций для группы", tags =
    ApplicationConfiguration.TAG_ADMIN_META)
  @GetMapping(value = "/config/{group}/meta")
  public List<BaseConfigurationMetaResponse> selectMetaByGroup(
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group) {
    final String name = name(token);
    return base
      .selectWithUsers(name)
      .stream()
      .filter(c -> c.getGroupUri().equals(group))
      .collect(Collectors.toList());
  }

  @Operation(summary = "Получение метаинформации конфигурации по URI", tags = ApplicationConfiguration.TAG_ADMIN_META)
  @GetMapping("/config/{group}/{uri}/meta")
  public List<BaseConfigurationMetaResponse> selectMetaByUrl(
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(token);
    return base.selectWithUsers(name, uri);
  }

  @Operation(summary = "Получение конфигурации по URI", tags = ApplicationConfiguration.TAG_ADMIN_CONFIG)
  @GetMapping("/config/{group}/{uri}")
  public String selectByUrl(
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(token);
    return base.selectConfig(name, uri);
  }

  @Operation(summary = "Редактирование конфигурации", tags = ApplicationConfiguration.TAG_ADMIN_CONFIG)
  @PutMapping("/config/{group}/{uri}")
  public void updateConfig
    (@RequestBody String value,
      @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
      @PathVariable(value = "group") String group,
      @PathVariable("uri") String uri) {
    final String name = name(token);
    base.updateConfig(value, name, uri);
  }

  @Operation(summary = "Редактирование метаинформации конфигурации", tags = ApplicationConfiguration.TAG_ADMIN_META)
  @PutMapping("/config/{group}/{uri}/meta")
  public void update
    (@RequestBody ConfigurationMetaRequest value,
      @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
      @PathVariable(value = "group") String group,
      @PathVariable("uri") String uri) {
    final String name = name(token);
    validateConfigurationMetaRequest(value);
    base.update(value, name, uri);
  }

  @Operation(summary = "Удаление метаинформации конфигурации", tags = ApplicationConfiguration.TAG_ADMIN_META)
  @DeleteMapping("/config/{group}/{uri}/meta")
  public void delete(
    @RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token,
    @PathVariable(value = "group") String group,
    @PathVariable("uri") String uri) {
    final String name = name(token);
    base.delete(uri, name);
  }

  @Operation(summary = "Список пользователей", tags = ApplicationConfiguration.TAG_ADMIN_META)
  @GetMapping("/users")
  public List<String> findUsers(@RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token) {
    name(token);
    return repository.findUsers();
  }

}
