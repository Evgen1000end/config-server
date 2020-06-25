package ru.demkin.esb.configserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
  private String token;
  private String username;
  private String fio;

  public LoginResponse() {
  }

  public LoginResponse(String token, String username, String fio) {
    this.token = token;
    this.username = username;
    this.fio = fio;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFio() {
    return fio;
  }

  public void setFio(String fio) {
    this.fio = fio;
  }
}
