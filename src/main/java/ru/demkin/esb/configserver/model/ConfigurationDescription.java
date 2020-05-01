package ru.demkin.esb.configserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationDescription {
  public static final ConfigurationDescription EMPTY = new ConfigurationDescription();

  private long id;
  private String uri;
  private String label;

  // @JsonDeserialize(using = NestedDeserializer.class)
  // @JsonSerialize(using = NestedSerializer.class)

  @JsonRawValue
  private String value;

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

  public String getValue() {
    return value;
  }

  @JsonProperty(value = "value")
  public void setValue(JsonNode node) {
    this.value = node.toString();
  }

}
