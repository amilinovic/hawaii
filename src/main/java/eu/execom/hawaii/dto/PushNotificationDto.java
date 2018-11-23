package eu.execom.hawaii.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushNotificationDto {
  @JsonProperty("registration_ids")
  private List<String> to;
  private NotificationDto notification;
  @JsonProperty("mutable_content")
  private boolean mutableContent;
  private NotificationDataDto data;
}
