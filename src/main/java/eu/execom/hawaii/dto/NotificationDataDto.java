package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.Data;

/**
 * Notification is made of four parts: "to", "notification", "mutableContent" and "data".
 * NotificationDataDto refers to part "data" and it is used by Android and iOS both.
 */
@Data
public class NotificationDataDto {

  private String body;
  private String title;
  private String priority;
  private RequestStatus requestStatus;
  private int requestId;
}
