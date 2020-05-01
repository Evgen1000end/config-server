package ru.demkin.esb.configserver.model;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ErrorResponse {

  private String error;

  private String details;

  public static ErrorResponse of(Throwable t) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setError(t.getMessage());
    errorResponse.setDetails(ExceptionUtils.getStackTrace(t));
    return errorResponse;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }
}
