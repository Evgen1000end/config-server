package ru.demkin.esb.configserver.model;

public class BaseConfigurationMetaResponse extends ConfigurationMetaResponse {
  private String username;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
