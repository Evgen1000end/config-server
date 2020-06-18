package ru.demkin.esb.configserver.services;

import ru.demkin.esb.configserver.model.BaseConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.ConfigurationMetaRequest;
import ru.demkin.esb.configserver.model.ConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.GroupRequest;
import ru.demkin.esb.configserver.model.GroupResponse;

import java.util.List;

public interface ConfigurationUpdateStrategy {

  // Низкоуровневые сервисы поиска
  void insert(ConfigurationMetaRequest description, String username, String group);

  void update(ConfigurationMetaRequest description, String username, String uri);

  void updateConfig(String config, String username, String uri);

  void updateConfigForAdmin(String config, String uri);

  void updateConfigForUser(String username, String config, String uri);

  void delete(String uri, String username);

  List<ConfigurationMetaResponse> select(String username);

  List<BaseConfigurationMetaResponse> selectWithUsers(String username);

  // API для групп
  void insertGroup(GroupRequest group);

  void updateGroup(GroupRequest group, String uri);

  void deleteGroup(String uri);

  List<GroupResponse> findAllGroups();

  GroupResponse findGroupByUri(String uri);

  ConfigurationMetaResponse select(String username, String uri);

  List<BaseConfigurationMetaResponse> selectWithUsers(String username, String uri);

  String selectConfig(String username, String uri);

  ConfigurationMetaResponse selectForUser(String username, String uri);

  ConfigurationMetaResponse selectForAdmin(String uri);

  String selectConfigForUser(String username, String uri);

  String selectConfigForAdmin(String uri);

  void deleteForAdmin(String uri);

  void updateForAdmin(ConfigurationMetaRequest description, String uri);

}
