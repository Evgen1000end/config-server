package ru.demkin.esb.configserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public class ConfigurationMetaResponse {
  protected long id;
  protected long groupId;
  protected String uri;
  protected String label;
  protected String groupUri;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public long getGroupId() {
    return groupId;
  }

  public void setGroupId(long groupId) {
    this.groupId = groupId;
  }

  public String getGroupUri() {
    return groupUri;
  }

  public void setGroupUri(String groupUri) {
    this.groupUri = groupUri;
  }
}
