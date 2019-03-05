package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.exceptions.ActionNotAllowedException;
import eu.execom.hawaii.exceptions.DuplicateEntryException;
import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.exceptions.NotAuthorizedApprovalException;
import eu.execom.hawaii.exceptions.RequestAlreadyCanceledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotAuthorizedApprovalException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, NotAuthorizedApprovalException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, AccessDeniedException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, EntityNotFoundException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, EntityExistsException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, DataIntegrityViolationException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RequestAlreadyCanceledException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, RequestAlreadyCanceledException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ActionNotAllowedException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, ActionNotAllowedException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DuplicateEntryException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, DuplicateEntryException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InsufficientHoursException.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, InsufficientHoursException ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()),
        HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handle(HttpServletRequest request, Exception ex) {
    logException(request, ex);
    return new ResponseEntity<>(Collections.singletonMap("error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private void logException(HttpServletRequest request, Exception ex) {
    log.error("Requesting resource: '{}' raised exception: '{}'", request.getRequestURI(), ex);
  }
}
