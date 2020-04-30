package ru.demkin.esb.configserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.repository.ConfigRepositoty;
import ru.demkin.esb.configserver.repository.UserRepository;

import java.util.List;

public class BaseConfigurationUpdateStrategy implements ConfigurationUpdateStrategy {

  @Autowired
  private ConfigRepositoty configRepositoty;

  @Override
  public void insert(ConfigurationDescription description, String username) {
    boolean isAdmin = username == null;
    configRepositoty.saveConfig(isAdmin, description, username, description.getValue().toString());
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
