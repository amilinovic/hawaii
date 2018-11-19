package eu.execom.hawaii.model;

import eu.execom.hawaii.model.enumerations.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
public class UserPushTokens extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -4766077773598671711L;

  @ManyToOne
  private User user;

  private String pushToken;

  private Platform platform;
}
