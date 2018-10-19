package eu.execom.hawaii.dto;

import lombok.Data;

@Data
public class NotificationDto {

  private String body;
  private String title;
  private String priority;
}
