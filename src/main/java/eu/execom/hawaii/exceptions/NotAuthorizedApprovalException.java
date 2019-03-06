package eu.execom.hawaii.exceptions;

public class NotAuthorizedApprovalException extends RuntimeException {

  private static final long serialVersionUID = 3319108758666227932L;

  public NotAuthorizedApprovalException(String handleResponse) {
    super("You are not authorized to " + handleResponse + " this request.");
  }
}
