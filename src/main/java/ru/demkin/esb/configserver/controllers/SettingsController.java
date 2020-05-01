package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Settings", description = "Настройки")
public class SettingsController {

  @Operation(summary = "Получение версии приложения")
  @GetMapping("/settings")
  public Settings version() {
    return new Settings("1.0.4");
  }

  public static class Settings {
    private String serverVersion;

    public Settings() {
    }

    public Settings(String serverVersion) {
      this.serverVersion = serverVersion;
    }

    public String getServerVersion() {
      return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
      this.serverVersion = serverVersion;
    }
  }

}
