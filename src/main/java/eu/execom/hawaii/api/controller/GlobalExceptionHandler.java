package eu.execom.hawaii.api.controller;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.exceptions.NotAuthorizedApprovalExeception;
import eu.execom.hawaii.exceptions.RequestAlreadyCanceledException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
  @ExceptionHandler(NotAuthorizedApprovalExeception.class)
  public void handleNotAuthorizedApprovalException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN) // 403
  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(EntityNotFoundException.class)
  public void handleEntityNotFoundException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  @ResponseStatus(HttpStatus.CONFLICT) // 409
  @ExceptionHandler(EntityExistsException.class)
  public void handleEntityExistsException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  @ResponseStatus(HttpStatus.CONFLICT) // 409
  @ExceptionHandler(DataIntegrityViolationException.class)
  public void handleDataIntegrityViolationException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  @ResponseStatus(HttpStatus.CONFLICT) // 409
  @ExceptionHandler(RequestAlreadyCanceledException.class)
  public void handleRequestAlreadyCanceledException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE) // 416
  @ExceptionHandler(InsufficientHoursException.class)
  public void handleInsufficientHoursException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
  @ExceptionHandler(Exception.class)
  public void handleException(HttpServletRequest request, Exception exception) {
    logException(request, exception);
  }

  private void logException(HttpServletRequest request, Exception exception) {
    log.error("Requesting resource: {} raised exception: {}", request.getRequestURI(), exception);
  }

}
