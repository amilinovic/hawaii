package eu.execom.hawaii.exceptions;

public class InsufficientHoursException extends RuntimeException {

  private static final long serialVersionUID = -8496947778753670980L;

  public InsufficientHoursException(String leaveType) {
    super("You don't have enough " + leaveType + " hours for this request.");
  }
}
