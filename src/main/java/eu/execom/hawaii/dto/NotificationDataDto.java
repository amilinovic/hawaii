package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.Data;

@Data
public class NotificationDataDto {

  private String body;
  private String title;
  private String priority;
  private RequestStatus requestStatus;
}
