package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.demkin.esb.configserver.ApplicationConfiguration;
import ru.demkin.esb.configserver.ApplicationProperties;
import ru.demkin.esb.configserver.Protocol;
import ru.demkin.esb.configserver.exception.ForbiddenException;
import ru.demkin.esb.configserver.model.ConfigurationMetaRequest;
import ru.demkin.esb.configserver.model.LoginRequest;
import ru.demkin.esb.configserver.model.LoginResponse;
import ru.demkin.esb.configserver.repository.SessionRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class AuthController {

  private final List<String> sessions = new ArrayList<>();

  private volatile boolean isLogged = false;

  private void validateLogin(LoginRequest request) {
    if (StringUtils.isBlank(request.getUsername())) {
      throw new IllegalArgumentException("username should not be blank");
    }
    if (StringUtils.isBlank(request.getPassword())) {
      throw new IllegalArgumentException("password should not be blank");
    }
  }

  private String createSession(LoginRequest request) {
    if (request.getPassword().equals("admin") && request.getUsername().equals("admin")) {
      final String session = UUID.randomUUID().toString();
      sessions.add(session);
      return session;
    } else {
      throw new ForbiddenException("Invalid credentials " + request);
    }
  }

  @PostMapping("/login")
  @Operation(summary = "Логин", tags = ApplicationConfiguration.TAG_AUTH)
  public LoginResponse login(@RequestBody LoginRequest request) {
    validateLogin(request);
    String session = createSession(request);
    isLogged = true;
    return new LoginResponse(session);
  }

  @PostMapping("/logout")
  @Operation(summary = "Логаут", tags = ApplicationConfiguration.TAG_AUTH)
  public void logout(@RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token) {
    final boolean tokenEmpty = StringUtils.isBlank(token);
    if (tokenEmpty) {
      throw new ForbiddenException("Access denied. Empty token for ADMIN.");
    } else {
      if (sessionValid(token)) {
        clearSession(token);
      } else {
        throw new ForbiddenException("Token " + token + " invalid");
      }
    }
    isLogged = false;
  }

  @GetMapping("/session")
  @Operation(summary = "Проверка сессии", tags = ApplicationConfiguration.TAG_AUTH)
  public ResponseEntity<Void> session(@RequestHeader(name = Protocol.HEADER_ADMIN, required = false) String token) {
    final boolean tokenEmpty = StringUtils.isBlank(token);
    if (tokenEmpty) {
      throw new ForbiddenException("Access denied. Empty token for ADMIN.");
    } else {
      if (isLogged()) {
        return ResponseEntity.status(HttpStatus.OK).build();
      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

      }
    }
  }

  public synchronized boolean sessionValid(String session) {
    return sessions.contains(session);
  }

  public synchronized void clearSession(String session) {
    sessions.removeIf(savedSession -> Objects.equals(savedSession, session));
  }

  public boolean isLogged() {
    return isLogged;
  }

}
