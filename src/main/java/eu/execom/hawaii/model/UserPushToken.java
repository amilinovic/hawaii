package eu.execom.hawaii.model;

import eu.execom.hawaii.model.enumerations.Platform;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserPushToken extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -4766077773598671711L;

  @ManyToOne
  private User user;

  @NotNull
  private String pushToken;

  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Platform platform;
}
