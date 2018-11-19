package eu.execom.hawaii.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PushNotificationDto {
  @JsonProperty("registration_ids")
  private List<String> to;
  private NotificationDto notification;
  private NotificationDataDto data;
}
