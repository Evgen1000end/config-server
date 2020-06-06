package ru.demkin.esb.configserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ConfigurationProperties("app")
public class ApplicationProperties {
  private boolean authEnabled = true;

  public boolean isAuthEnabled() {
    return authEnabled;
  }

  public void setAuthEnabled(boolean authEnabled) {
    this.authEnabled = authEnabled;
  }

  @Override
  public String toString() {
    return "ApplicationProperties{" +
      "authEnabled=" + authEnabled +
      '}';
  }
}
