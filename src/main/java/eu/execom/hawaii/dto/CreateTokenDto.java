package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.enumerations.Platform;
import lombok.Data;

@Data
public class CreateTokenDto {

  private String pushToken;
  private Platform platform;
  private String name;

  public CreateTokenDto(UserPushToken userPushToken) {
    this.pushToken = userPushToken.getPushToken();
    this.platform = userPushToken.getPlatform();
    this.name = userPushToken.getName();
  }
}
