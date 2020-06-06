package ru.demkin.esb.configserver.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.demkin.esb.configserver.Utils;
import ru.demkin.esb.configserver.exception.AlreadyExistException;
import ru.demkin.esb.configserver.exception.NotFoundException;
import ru.demkin.esb.configserver.model.ConfigDescriptionDto;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.model.Group;
import ru.demkin.esb.configserver.repository.BaseConfigRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BaseConfigurationUpdateStrategy implements ConfigurationUpdateStrategy {

  @Autowired
  private BaseConfigRepository base;

  private String json(ConfigDescriptionDto dto) {
//    final String value = dto.getValue();
//    final ObjectMapper mapper = new ObjectMapper();
//    JsonNode actualObj = null;
//    try {
//      actualObj = mapper.readTree(value);
//    } catch (Exception e) {
//      return "{}";
//    }
//    return actualObj.toString();
    return dto.getValue();
  }

  private ConfigurationDescription convert(ConfigDescriptionDto dto) {
    final ConfigurationDescription config = new ConfigurationDescription();
    config.setId(dto.getId());
    config.setLabel(dto.getLabel());
    config.setUri(dto.getUri());
    config.setGroupId(dto.getGroupId());
    config.setGroupUri(dto.getGroupUri());

//    final String value = configDescriptionDto.getValue();
//    final ObjectMapper mapper = new ObjectMapper();
//    JsonNode actualObj = null;
//    try {
//      actualObj = mapper.readTree(value);
//    } catch (JsonProcessingException e) {
//      e.printStackTrace();
//    }

    // TODO - удалить!
    //configurationDescription.setValue(actualObj);

    return config;
  }

  @Override
  public void insert(ConfigurationDescription description, String username, String group) {
    final boolean isAdmin = username == null;

    final Group groupByUri = findGroupByUri(group);
    final String uri = description.getUri();
    final List<ConfigDescriptionDto> configs = base.findConfigsByUrl(username, isAdmin, uri);
    if (!configs.isEmpty()) {
      throw new AlreadyExistException("Configuration already exist for " + Utils.user(username) + " and uri = " + uri);
    } else {
      final long groupId = groupByUri.getId();
      base.saveConfig(isAdmin, description, username, groupId, group);
    }
  }

  @Override
  public void update(ConfigurationDescription description, String username, String uri) {
    final boolean isAdmin = username == null;
    select(username, uri);
    base.updateConfig(isAdmin, description, username, uri);
  }

  @Override
  public void updateConfig(String config, String username, String uri) {
    final boolean isAdmin = username == null;
    select(username, uri);
    base.updateConfigValue(isAdmin, config, username, uri);
  }

  @Override
  public void updateConfigForAdmin(String config, String uri) {
    selectConfig(null, uri);
    base.deleteConfigByUri2(uri);
    base.updateConfigValue(true, config, null, uri);
  }

  @Override
  public void updateConfigForUser(String username, String config, String uri) {
    final boolean isAdmin = username == null;
    selectConfig(username, uri);
    base.updateConfigValue(isAdmin, config, username, uri);
  }

  @Override
  public void delete(String uri, String username) {
    final boolean isAdmin = isAdmin(username);
    select(username, uri);
    base.deleteConfig(uri, username, isAdmin);
  }

  @Override
  public List<ConfigurationDescription> select(String username) {
    return base.findConfigs(username, isAdmin(username))
      .stream()
      .map(this::convert)
      .collect(Collectors.toList());
  }

  @Override
  public void insertGroup(Group group) {
    List<Group> groups = base.findGroupByUrl(group.getUri());
    if (!groups.isEmpty()) {
      throw new AlreadyExistException("Group " + group.getUri() + "  already exist");
    } else {
      base.saveGroup(group);
    }

  }

  @Override
  public void updateGroup(Group group, String uri) {
    findGroupByUri(uri);
    base.updateGroup(group, uri);
  }

  @Override
  public void deleteGroup(String uri) {
    findGroupByUri(uri);
    base.deleteGroup(uri);
  }

  @Override
  public List<Group> findAllGroups() {
    return base.findGroups();
  }

  @Override
  public Group findGroupByUri(String uri) {
    final List<Group> groups = base.findGroupByUrl(uri);
    if (groups.isEmpty()) {
      throw new NotFoundException("Group " + uri + " not found.");
    } else {
      return groups.get(0);
    }
  }

  @Override
  public ConfigurationDescription select(String username, String uri) {
    final List<ConfigDescriptionDto> configDto = base.findConfigsByUrl(username, isAdmin(username), uri);
    if (configDto.isEmpty()) {
      throw new NotFoundException("Configuration not found for " + Utils.user(username) + " and uri = " + uri);
    } else {
      return convert(configDto.get(0));
    }
  }

  @Override
  public String selectConfig(String username, String uri) {
    final List<ConfigDescriptionDto> configDto = base.findConfigsByUrl(username, isAdmin(username), uri);
    if (configDto.isEmpty()) {
      throw new NotFoundException("Configuration not found for " + Utils.user(username) + " and uri = " + uri);
    } else {
      return json(configDto.get(0));
    }
  }

  private boolean configExist(String username, String uri) {
    List<ConfigDescriptionDto> configDto = base.findConfigsByUrl(username, isAdmin(username), uri);
    return !configDto.isEmpty();
  }

  @Override
  public ConfigurationDescription selectForUser(String username, String uri) {
    // Проверяем есть ли пользовательский конфиг?
    boolean userConfigExist = configExist(username, uri);
    if (userConfigExist) {
      return select(username, uri);
    } else {
      // нашли админский конфиг
      final ConfigurationDescription configurationDescription = selectForAdmin(uri);
      // Добавили пользовательский на основе админского
      insert(configurationDescription, username, configurationDescription.getGroupUri());
      return configurationDescription;
    }
  }

  @Override
  public ConfigurationDescription selectForAdmin(String uri) {
    // Проверяем есть ли вообще админский конфиг
    return select(null, uri);
  }

  @Override
  public String selectConfigForUser(String username, String uri) {
    boolean userConfigExist = configExist(username, uri);
    if (userConfigExist) {
      return selectConfig(username, uri);
    } else {
      // нашли админский конфиг
      final ConfigurationDescription configurationDescription = selectForAdmin(uri);
      // Добавили пользовательский на основе админского
      insert(configurationDescription, username, configurationDescription.getGroupUri());
      String value = selectConfigForAdmin(uri);
      updateConfig(value, username, uri);

      return selectConfig(username, uri);
    }
  }

  @Override
  public String selectConfigForAdmin(String uri) {
    return selectConfig(null, uri);
  }

  @Override
  public void deleteForAdmin(String uri) {
    select(null, uri);
    base.deleteConfigByUri(uri);

  }

  @Override
  public void updateForAdmin(ConfigurationDescription description, String uri) {
    select(null, uri);
    //  base.deleteConfigByUri2(uri);
    base.updateConfig(true, description, null, uri);
  }

  private boolean isAdmin(String username) {
    return username == null;
  }

}

