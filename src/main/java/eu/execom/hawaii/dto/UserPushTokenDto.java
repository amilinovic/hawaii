package eu.execom.hawaii.dto;

import lombok.Data;

@Data
public class UserPushTokenDto {

  private String uid;

  private String pushToken;
}
