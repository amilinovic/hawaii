package eu.execom.hawaii.util;

public final class EmailSubjectProvider {

  private EmailSubjectProvider() {
  }

  public static final String REQUEST_CREATED_SUBJECT = "Leave Request requires your Approval";

  public static final String TEAM_NOTIFICATION_SICKNESS_SUBJECT = "Sickness Notification";

  public static final String TEAM_NOTIFICATION_ANNUAL_SUBJECT = "Annual Leave Notification";

  private static final String LEAVE_REQUEST_NOTIFICATION_SUBJECT = "Leave Request %s";

  public static String getLeaveRequestNotificationSubject(String status) {
    return String.format(LEAVE_REQUEST_NOTIFICATION_SUBJECT, status);
  }

}
