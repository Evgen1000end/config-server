package ru.demkin.esb.configserver.services;

import ru.demkin.esb.configserver.model.ConfigurationDescription;

import java.util.List;

public interface ConfigurationUpdateStrategy {

  // Низкоуровневые сервисы поиска
  void insert(ConfigurationDescription description, String username);

  void update(ConfigurationDescription description, String username, String uri);

  void delete(String uri, String username);

  List<ConfigurationDescription> select(String username);

  ConfigurationDescription select(String username, String uri);

  ConfigurationDescription selectForUser(String username, String uri);

  ConfigurationDescription selectForAdmin(String uri);

  void deleteForAdmin(String uri);

  void updateForAdmin(ConfigurationDescription description, String uri);

}
