package ru.demkin.esb.configserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.demkin.esb.configserver.Utils;
import ru.demkin.esb.configserver.exception.AlreadyExistException;
import ru.demkin.esb.configserver.exception.NotFoundException;
import ru.demkin.esb.configserver.model.BaseConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.ConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.ConfigurationMetaRequest;
import ru.demkin.esb.configserver.model.GroupRequest;
import ru.demkin.esb.configserver.model.GroupResponse;
import ru.demkin.esb.configserver.repository.BaseConfigRepository;

import java.util.List;

public class BaseConfigurationUpdateStrategy implements ConfigurationUpdateStrategy {

  @Autowired
  private BaseConfigRepository base;

  @Override
  public void insert(ConfigurationMetaRequest description, String username, String group) {
    final boolean isAdmin = username == null;

    final GroupResponse groupByUri = findGroupByUri(group);
    final String uri = description.getUri();
    final List<ConfigurationMetaResponse> configs = base.findMetaByUrl(username, isAdmin, uri);
    if (!configs.isEmpty()) {
      throw new AlreadyExistException("Configuration already exist for " + Utils.user(username) + " and uri = " + uri);
    } else {
      final long groupId = groupByUri.getId();
      base.saveConfig(isAdmin, description, username, groupId, group);
    }
  }

  @Override
  public void update(ConfigurationMetaRequest description, String username, String uri) {
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
  public List<ConfigurationMetaResponse> select(String username) {
    return base.findMeta(username, isAdmin(username));
  }

  @Override
  public List<BaseConfigurationMetaResponse> selectWithUsers(String username) {
    return base.findMetaWithUsers();
  }

  @Override
  public void insertGroup(GroupRequest group) {
    List<GroupResponse> groups = base.findGroupByUrl(group.getUri());
    if (!groups.isEmpty()) {
      throw new AlreadyExistException("Group " + group.getUri() + "  already exist");
    } else {
      base.saveGroup(group);
    }

  }

  @Override
  public void updateGroup(GroupRequest group, String uri) {
    findGroupByUri(uri);
    base.updateGroup(group, uri);
  }

  @Override
  public void deleteGroup(String uri) {
    findGroupByUri(uri);
    base.deleteGroup(uri);
  }

  @Override
  public List<GroupResponse> findAllGroups() {
    return base.findGroups();
  }

  @Override
  public GroupResponse findGroupByUri(String uri) {
    final List<GroupResponse> groups = base.findGroupByUrl(uri);
    if (groups.isEmpty()) {
      throw new NotFoundException("Group " + uri + " not found.");
    } else {
      return groups.get(0);
    }
  }

  @Override
  public ConfigurationMetaResponse select(String username, String uri) {
    final List<ConfigurationMetaResponse> configDto = base.findMetaByUrl(username, isAdmin(username), uri);
    if (configDto.isEmpty()) {
      throw new NotFoundException("Configuration not found for " + Utils.user(username) + " and uri = " + uri);
    } else {
      return configDto.get(0);
    }
  }

  @Override
  public List<BaseConfigurationMetaResponse> selectWithUsers(String username, String uri) {
    return base.findMetaByUrlWithUsers(uri);
  }

  @Override
  public String selectConfig(String username, String uri) {
    final List<String> configDto = base.findConfigByUrl(username, isAdmin(username), uri);
    if (configDto.isEmpty()) {
      throw new NotFoundException("Configuration not found for " + Utils.user(username) + " and uri = " + uri);
    } else {
      return configDto.get(0);
    }
  }

  private boolean configExist(String username, String uri) {
    List<ConfigurationMetaResponse> configDto = base.findMetaByUrl(username, isAdmin(username), uri);
    return !configDto.isEmpty();
  }

  @Override
  public ConfigurationMetaResponse selectForUser(String username, String uri) {
    // Проверяем есть ли пользовательский конфиг?
    boolean userConfigExist = configExist(username, uri);
    if (userConfigExist) {
      return select(username, uri);
    } else {
      // нашли админский конфиг
      final ConfigurationMetaResponse configurationDescription = selectForAdmin(uri);
      // Добавили пользовательский на основе админского
      insert(from(configurationDescription), username, configurationDescription.getGroupUri());
      return configurationDescription;
    }
  }

  ConfigurationMetaRequest from(ConfigurationMetaResponse response) {
    ConfigurationMetaRequest request = new ConfigurationMetaRequest();
    request.setLabel(response.getLabel());
    request.setUri(response.getUri());
    return request;
  }

  @Override
  public ConfigurationMetaResponse selectForAdmin(String uri) {
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
      final ConfigurationMetaResponse configurationDescription = selectForAdmin(uri);
      // Добавили пользовательский на основе админского
      insert(from(configurationDescription), username, configurationDescription.getGroupUri());
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
  public void updateForAdmin(ConfigurationMetaRequest description, String uri) {
    select(null, uri);
    //  base.deleteConfigByUri2(uri);
    base.updateConfig(true, description, null, uri);
  }

  private boolean isAdmin(String username) {
    return username == null;
  }

}

