package eu.execom.hawaii.util;

import java.time.LocalDate;

public class EmailFormatter {

  public static final String REQUEST_CREATED_SUBJECT = "Leave Request requires your Approval";
  private static final String REQUEST_CREATED_TEXT = "The following Leave Request has been submitted by %s:\n\n"
      + "Please login to the Hawaii system to Approve or Reject the request.\n\n"
      + "Leave Request Details\n"
      + "Requested by: %s\n"
      + "Leave type: %s\n"
      + "Requested dates: %s - %s\n"
      + "Number of days: %d\n"
      + "Reason for leave: %s\n\n"
      + "Approval\n"
      + "Your approval for this request is required.\n"
      + "--------------------------------------------------------\n"
      + "This is a notification email from the Hawaii HR system. Please do net reply directly to this email.";

  public static String getRequestCreatedText(String userFullName, String absenceName, LocalDate startDate,
      LocalDate endDate, int numberOfDays, String reason) {
    return String.format(REQUEST_CREATED_TEXT, userFullName, userFullName, absenceName, startDate, endDate,
        numberOfDays, reason);
  }

  public static final String TEAM_NOTIFICATION_SICKNESS_SUBJECT = "Sickness Notification";
  private static final String TEAM_NOTIFICATION_SICKNESS_TEXT = "This is a notification from the Hawaii system.\n\n"
      + "A sickness report for %s has been submitted.\n\n"
      + "You are receiving this notification because you are listed as a Notifier for the %s team.\n"
      + "To be removed from this list, please contact your Hawaii Administrator.\n\n"
      + "Sickness Report Details\n"
      + "Request by: %s\n"
      + "Requested dates: %s - %s\n"
      + "Number of days: %d\n"
      + "Reason for Sickness: %s\n\n"
      + "--------------------------------------------------------\n"
      + "This is a notification email from the Hawaii HR system. Please do net reply directly to this email.";


  private static final String LEAVE_REQUEST_NOTIFICATION_SUBJECT = "Leave Request %s";
  private static final String LEAVE_REQUEST_NOTIFICATION_TEXT = "Hi, %s\n"
      + "The following Leave Request has been %s:\n\n"
      + "Leave Request Details\n"
      + "Leave type: %s\n"
      + "Requested dates: %s-%s\n"
      + "Number of days: %d\n"
      + "Reason for leave: %s";

  public static String getLeaveRequestNotificationSubject(String status) {
    return String.format(LEAVE_REQUEST_NOTIFICATION_SUBJECT, status);
  }

  public static String getLeaveRequestApprovedSubject(String userFullName, String status, String absenceName,
      LocalDate startDate, LocalDate endDate, int numberOfDays, String reason) {
    return String.format(LEAVE_REQUEST_NOTIFICATION_TEXT, userFullName, status, absenceName, startDate, endDate,
        numberOfDays, reason);
  }


}
