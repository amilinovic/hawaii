package eu.execom.hawaii.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@AllArgsConstructor
public class GenericResponse {

  public static final GenericResponse OK = new GenericResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);

  private int code;

  private String message;

  private Object data;

  public GenericResponse(Object data) {
    this.code = HttpStatus.OK.value();
    this.message = HttpStatus.OK.getReasonPhrase();
    this.data = data;
  }

  public GenericResponse(HttpStatus status, Object data) {
    this.code = status.value();
    this.message = status.getReasonPhrase();
    this.data = data;
  }
}
