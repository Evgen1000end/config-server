package ru.demkin.esb.configserver.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.demkin.esb.configserver.model.ErrorResponse;

//@RestController
public class ErrorProcessor implements ErrorController {

  private static final String PATH = "/error";

  @RequestMapping(value = PATH)
  public ResponseEntity<ErrorResponse> error(Exception e) {
    ErrorResponse response = ErrorResponse.of(e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }
}
