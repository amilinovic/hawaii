package eu.execom.hawaii.api.controller;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(EntityNotFoundException.class)
  public void handleEntityNotFoundException(HttpServletRequest request, Exception exception) {
    log.error("Requesting resource: {} raised exception: {}", request.getRequestURI(), exception);
  }

}
