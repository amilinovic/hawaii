package eu.execom.hawaii.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PushNotificationDto {
  @JsonProperty("to")
  private String to;
  @JsonProperty("notification")
  private NotificationDto notification;
  @JsonProperty("data")
  private NotificationDto data;
}
