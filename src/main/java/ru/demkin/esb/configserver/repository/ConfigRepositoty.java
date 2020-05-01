package ru.demkin.esb.configserver.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import ru.demkin.esb.configserver.model.ConfigDescriptionDto;
import ru.demkin.esb.configserver.model.ConfigurationDescription;

import java.util.List;

@Repository
@Mapper
public interface ConfigRepositoty {

  void saveConfig(boolean isAdmin, ConfigurationDescription description, String username, String value);

  List<ConfigDescriptionDto> findAdminConfigs();
}
