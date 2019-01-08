package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.UserPushToken;
import lombok.Data;

@Data
public class EditPushTokenDto {
  private Long oldPushTokenId;
  private String newPushToken;

  public EditPushTokenDto(UserPushToken userPushToken) {
    this.oldPushTokenId = userPushToken.getId();
    this.newPushToken = userPushToken.getPushToken();
  }
}
