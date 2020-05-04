package ru.demkin.esb.configserver.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.demkin.esb.configserver.exception.AlreadyExistException;
import ru.demkin.esb.configserver.exception.NotFoundException;
import ru.demkin.esb.configserver.model.ErrorResponse;

@ControllerAdvice
public class ErrorController {

  @ExceptionHandler(value = NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
    ErrorResponse response = ErrorResponse.of(e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> allException(Exception e) {
    ErrorResponse response = ErrorResponse.of(e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(value = AlreadyExistException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> alreadyExistException(AlreadyExistException e) {
    ErrorResponse response = ErrorResponse.of(e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

}
