package eu.execom.hawaii.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.persistence.Entity;

@Data
public class GenericResponse {

  private final int code;

  private final String message;

  private final Object data;

  public static final GenericResponse OK = new GenericResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);

  public GenericResponse(Object data) {
    this.code = HttpStatus.OK.value();
    this.message = HttpStatus.OK.getReasonPhrase();
    this.data = data;
  }

  public GenericResponse(int code, String message, Object data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public GenericResponse(HttpStatus status, Object data) {
    this.code = status.value();
    this.message = status.getReasonPhrase();
    this.data = data;
  }
}
