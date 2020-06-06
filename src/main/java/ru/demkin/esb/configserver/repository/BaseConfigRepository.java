package ru.demkin.esb.configserver.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.demkin.esb.configserver.model.ConfigDescriptionDto;
import ru.demkin.esb.configserver.model.ConfigurationDescription;
import ru.demkin.esb.configserver.model.Group;

import java.util.List;

@Repository
@Mapper
public interface BaseConfigRepository {

  void saveConfig(boolean isAdmin, ConfigurationDescription description, String username);

  List<ConfigDescriptionDto> findConfigs(String username, boolean isAdmin);

  List<ConfigDescriptionDto> findConfigsByUrl(String username, boolean isAdmin, String uri);

  void deleteConfig(String uri, String username, boolean isAdmin);

  void updateConfig(boolean isAdmin, ConfigurationDescription description, String username, String uri);

  void deleteConfigByUri(String uri);

  void deleteConfigByUri2(String uri);


  void saveGroup(@Param("groups") Group groups);

  List<Group> findGroups();

  List<Group> findGroupByUrl(String uri);

  void deleteGroup(String uri);

  void updateGroup(@Param("groups") Group groups, String uri);



}
