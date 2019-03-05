package eu.execom.hawaii.exceptions;

public class DuplicateEntryException extends RuntimeException {

  private static final long serialVersionUID = 6438799664929018147L;

  public DuplicateEntryException(String message) {
    super(message);
  }
}
