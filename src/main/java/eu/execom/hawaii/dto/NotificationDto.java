package eu.execom.hawaii.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Notification is made of four parts: "to", "notification", "mutableContent" and "data".
 * NotificationDto refers to part "notification" and it is used by iOS only.
 */
@Data
public class NotificationDto {

  private String body;
  private String title;
  private String priority;
  @JsonProperty("click_action")
  private String clickAction;
}
