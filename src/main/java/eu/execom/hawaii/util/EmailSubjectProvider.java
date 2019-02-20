package eu.execom.hawaii.util;

public final class EmailSubjectProvider {

  private EmailSubjectProvider() {
  }

  public static final String REQUEST_CREATED_SUBJECT = "Leave Request requires your Approval";

  public static final String SICKNESS_SUBJECT = "Sickness Notification";

  public static final String ANNUAL_SUBJECT = "Annual Leave Notification";

  public static final String BONUS_SUBJECT = "Bonus Leave Notification";

  private static final String LEAVE_REQUEST_NOTIFICATION_SUBJECT = "Leave Request %s";

  public static final String USER_LEAVE_PROFILE_UPDATE = "Leave Profile Update Notification";

  public static String getLeaveRequestNotificationSubject(String status) {
    return String.format(LEAVE_REQUEST_NOTIFICATION_SUBJECT, status);
  }

}
