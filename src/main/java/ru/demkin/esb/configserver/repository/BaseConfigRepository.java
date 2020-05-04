package ru.demkin.esb.configserver.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import ru.demkin.esb.configserver.model.ConfigDescriptionDto;
import ru.demkin.esb.configserver.model.ConfigurationDescription;

import java.util.List;

@Repository
@Mapper
public interface BaseConfigRepository {

  void saveConfig(boolean isAdmin, ConfigurationDescription description, String username, String value);

  List<ConfigDescriptionDto> findConfigs(String username, boolean isAdmin);

  List<ConfigDescriptionDto> findConfigsByUrl(String username, boolean isAdmin, String uri);

  void deleteConfig(String uri, String username, boolean isAdmin);

  void updateConfig(boolean isAdmin, ConfigurationDescription description, String username, String value, String uri);

  void deleteConfigByUri(String uri);

  void deleteConfigByUri2(String uri);

}
