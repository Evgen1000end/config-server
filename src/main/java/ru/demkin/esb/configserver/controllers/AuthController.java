package ru.demkin.esb.configserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.demkin.esb.configserver.ApplicationConfiguration;
import ru.demkin.esb.configserver.Protocol;
import ru.demkin.esb.configserver.exception.ForbiddenException;
import ru.demkin.esb.configserver.model.ConfigurationMetaRequest;
import ru.demkin.esb.configserver.model.LoginRequest;
import ru.demkin.esb.configserver.model.LoginResponse;
import ru.demkin.esb.configserver.repository.SessionRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class AuthController {

  private final List<String> sessions = new ArrayList<>();

  @PostConstruct
  public void init() {
    sessions.add(Protocol.MASTER_TOKEN);
  }

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
    return new LoginResponse(session);
  }



  public boolean sessionValid(String session) {
    return sessions.contains(session);
  }
}
