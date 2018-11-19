package eu.execom.hawaii.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Notification is made of four parts: "to", "notification", "mutableContent" and "data".
 * MutableContent is used only by iOS.
 */

@Data
public class PushNotificationDto {
  @JsonProperty("registration_ids")
  private List<String> to;
  private NotificationDto notification;
  @JsonProperty("mutable_content")
  private boolean mutableContent;
  private NotificationDataDto data;
}
