package ru.demkin.esb.configserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.repository.ConfigRepositoty;

import java.util.List;

public class UserConfigurationUpdateStrategy implements ConfigurationUpdateStrategy {

  @Autowired
  private ConfigRepositoty repositoty;

  @Override
  public void insert(ConfigurationDescription description, String username) {

  }

  @Override
  public void update(ConfigurationDescription description, String username) {

  }

  @Override
  public void delete(String uri) {

  }

  @Override
  public List<ConfigurationDescription> select() {
    return null;
  }

  @Override
  public ConfigurationDescription select(String uri) {
    return null;
  }
}