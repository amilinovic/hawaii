package eu.execom.hawaii.exceptions;

public class ActionNotAllowedException extends RuntimeException {

  private static final long serialVersionUID = 7051154292940038129L;

  public ActionNotAllowedException(String message) {
    super(message);
  }
}
