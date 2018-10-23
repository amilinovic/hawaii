package eu.execom.hawaii.dto;

import lombok.Data;

import java.util.List;

@Data
public class PushNotificationToApproversDto {

  private List<String> registration_ids;
  private NotificationDto notification;
  private NotificationDataDto data;
}
