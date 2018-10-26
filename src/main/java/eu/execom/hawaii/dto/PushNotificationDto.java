package eu.execom.hawaii.dto;

import lombok.Data;

@Data
public class PushNotificationDto {
  private String to;
  private NotificationDto notification;
  private NotificationDataDto data;
}
