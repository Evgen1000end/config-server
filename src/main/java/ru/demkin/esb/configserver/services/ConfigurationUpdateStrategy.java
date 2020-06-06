package ru.demkin.esb.configserver.services;

import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.model.Group;

import java.util.List;

public interface ConfigurationUpdateStrategy {

  // Низкоуровневые сервисы поиска
  void insert(ConfigurationDescription description, String username, String group);

  void update(ConfigurationDescription description, String username, String uri);

  void updateConfig(String config, String username, String uri);

  void updateConfigForAdmin(String config, String uri);

  void updateConfigForUser(String username, String config, String uri);

  void delete(String uri, String username);

  List<ConfigurationDescription> select(String username);

  // API для групп
  void insertGroup(Group group);

  void updateGroup(Group group, String uri);

  void deleteGroup(String uri);

  List<Group> findAllGroups();

  Group findGroupByUri(String uri);

  ConfigurationDescription select(String username, String uri);

  String selectConfig(String username, String uri);

  ConfigurationDescription selectForUser(String username, String uri);

  ConfigurationDescription selectForAdmin(String uri);

  String selectConfigForUser(String username, String uri);

  String selectConfigForAdmin(String uri);

  void deleteForAdmin(String uri);

  void updateForAdmin(ConfigurationDescription description, String uri);

}
