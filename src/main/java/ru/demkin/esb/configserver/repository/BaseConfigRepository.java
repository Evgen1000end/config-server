package ru.demkin.esb.configserver.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.demkin.esb.configserver.model.BaseConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.ConfigurationMetaResponse;
import ru.demkin.esb.configserver.model.ConfigurationMetaRequest;
import ru.demkin.esb.configserver.model.GroupRequest;
import ru.demkin.esb.configserver.model.GroupResponse;

import java.util.List;

@Repository
@Mapper
public interface BaseConfigRepository {

  void saveConfig(boolean isAdmin, ConfigurationMetaRequest description, String username, long groupId,
    String groupUrl);

  List<ConfigurationMetaResponse> findMeta(String username, boolean isAdmin);

  List<BaseConfigurationMetaResponse> findMetaWithUsers();

  List<ConfigurationMetaResponse> findMetaByUrl(String username, boolean isAdmin, String uri);

  List<BaseConfigurationMetaResponse> findMetaByUrlWithUsers(String uri);

  List<String> findConfig(String username, boolean isAdmin);

  List<String> findConfigByUrl(String username, boolean isAdmin, String uri);

  void deleteConfig(String uri, String username, boolean isAdmin);

  void updateConfig(boolean isAdmin, ConfigurationMetaRequest description, String username, String uri);

  void updateConfigValue(boolean isAdmin, String config, String username, String uri);

  void deleteConfigByUri(String uri);

  void deleteConfigByUri2(String uri);

  void saveGroup(@Param("groups") GroupRequest groups);

  List<GroupResponse> findGroups();

  List<GroupResponse> findGroupByUrl(String uri);

  void deleteGroup(String uri);

  void updateGroup(@Param("groups") GroupRequest groups, String uri);

}
