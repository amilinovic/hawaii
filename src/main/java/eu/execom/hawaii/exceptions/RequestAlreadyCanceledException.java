package eu.execom.hawaii.exceptions;

public class RequestAlreadyCanceledException extends RuntimeException {

  private static final long serialVersionUID = 6183033209371798554L;

  public RequestAlreadyCanceledException() {
    super("This request has already been canceled.");
  }
}
