package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.enumerations.Platform;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPushTokenDto {
  private Long pushTokenId;
  private String pushToken;
  private String name;
  private Platform platform;
  private LocalDateTime createDateTime;

  public UserPushTokenDto(UserPushToken userPushToken) {
    this.pushTokenId = userPushToken.getId();
    this.pushToken = userPushToken.getPushToken();
    this.name = userPushToken.getName();
    this.platform = userPushToken.getPlatform();
    this.createDateTime = userPushToken.getCreateDateTime();
  }
}
