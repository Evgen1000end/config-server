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
    ConfigurationDescription configurationDescription = new ConfigurationDescription();
    configurationDescription.setId(configDescriptionDto.getId());
    configurationDescription.setLabel(configDescriptionDto.getLabel());
    configurationDescription.setUri(configDescriptionDto.getUri());

    String value = configDescriptionDto.getValue();

    ObjectMapper mapper = new ObjectMapper();
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
    final Optional<ConfigurationDescription> configurationDescription = selectInternal(username, uri);
    if (configurationDescription.isPresent()) {
      throw new AlreadyExistException("Configuration already exist for " + Utils.user(username) + "/" + uri);
    } else {
      configRepositoty.saveConfig(isAdmin, description, username, description.getValue());
    }
  }

  @Override
  public void update(ConfigurationDescription description, String username) {
  }

  @Override
  public void delete(String uri) {

  }

  @Override
  public List<ConfigurationDescription> select(String username) {
    if (username == null) {
      return configRepositoty.findAdminConfigs()
        .stream()
        .map(this::convert)
        .collect(Collectors.toList());
    } else {
      throw new IllegalStateException("Not implemented yet!");
    }
  }

  @Override
  public ConfigurationDescription select(String username, String uri) {
    return selectInternal(username, uri).orElseThrow(() -> new NotFoundException("Configuration not found for " + Utils.user(username) + "/" + uri));
  }

  private Optional<ConfigurationDescription> selectInternal(String username, String uri) {
    return select(username)
      .stream()
      .filter(description -> description.getUri().equals(uri))
      .findFirst();
  }
}

