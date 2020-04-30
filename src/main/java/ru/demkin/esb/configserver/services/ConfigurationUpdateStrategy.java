package ru.demkin.esb.configserver.services;

import ru.demkin.esb.configserver.model.ConfigurationDescription;

import java.util.List;

public interface ConfigurationUpdateStrategy {

  void insert(ConfigurationDescription description, String username);

  void update(ConfigurationDescription description, String username);

  void delete(String uri);

  List<ConfigurationDescription> select();

  ConfigurationDescription select(String uri);
}
