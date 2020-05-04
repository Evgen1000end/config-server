package ru.demkin.esb.configserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.demkin.esb.configserver.Utils;
import ru.demkin.esb.configserver.exception.AlreadyExistException;
import ru.demkin.esb.configserver.exception.NotFoundException;
import ru.demkin.esb.configserver.model.ConfigDescriptionDto;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.repository.ConfigRepositoty;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseConfigurationUpdateStrategy implements ConfigurationUpdateStrategy {

  @Autowired
  private ConfigRepositoty configRepositoty;

  private ConfigurationDescription convert(ConfigDescriptionDto configDescriptionDto) {
    final ConfigurationDescription configurationDescription = new ConfigurationDescription();
    configurationDescription.setId(configDescriptionDto.getId());
    configurationDescription.setLabel(configDescriptionDto.getLabel());
    configurationDescription.setUri(configDescriptionDto.getUri());
    final String value = configDescriptionDto.getValue();
    final ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = null;
    try {
      actualObj = mapper.readTree(value);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    configurationDescription.setValue(actualObj);
    return configurationDescription;
  }

  @Override
  public void insert(ConfigurationDescription description, String username) {
    final boolean isAdmin = username == null;
    final String uri = description.getUri();
    final List<ConfigDescriptionDto> configs = configRepositoty.findConfigsByUrl(username, isAdmin, uri);
    if (!configs.isEmpty()) {
      throw new AlreadyExistException("Configuration already exist for " + Utils.user(username) + "/" + uri);
    } else {
      configRepositoty.saveConfig(isAdmin, description, username, description.getValue());
    }
  }

  @Override
  public void update(ConfigurationDescription description, String username, String uri) {
    final boolean isAdmin = username == null;
    select(username, uri);
    configRepositoty.updateConfig(isAdmin, description, username, description.getValue(), uri);
  }

  @Override
  public void delete(String uri, String username) {
    final boolean isAdmin = isAdmin(username);
    select(username, uri);
    configRepositoty.deleteConfig(uri, username, isAdmin);
  }

  @Override
  public List<ConfigurationDescription> select(String username) {
    return configRepositoty.findConfigs(username, isAdmin(username))
      .stream()
      .map(this::convert)
      .collect(Collectors.toList());
  }

  @Override
  public ConfigurationDescription select(String username, String uri) {
    List<ConfigDescriptionDto> configDto = configRepositoty.findConfigsByUrl(username, isAdmin(username), uri);
    if (configDto.isEmpty()) {
      throw new NotFoundException("Configuration not found for " + Utils.user(username) + "/" + uri);
    } else {
      return convert(configDto.get(0));
    }
  }

  private boolean isAdmin(String username) {
    return username == null;
  }

}

