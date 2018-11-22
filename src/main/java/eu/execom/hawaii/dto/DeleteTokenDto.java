package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.UserPushToken;
import lombok.Data;

@Data
public class DeleteTokenDto {
  private String pushToken;

  public DeleteTokenDto(UserPushToken userPushToken){
    this.pushToken = userPushToken.getPushToken();
  }
}
