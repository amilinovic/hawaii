package eu.execom.hawaii.model.enumerations;

import java.util.Arrays;

public enum UserStatusType {
  ACTIVE("active"),
  INACTIVE("inactive"),
  DELETED("deleted");

  private String value;

  private UserStatusType(String value) {
    this.value = value;
  }

  public static UserStatusType fromValue(String value) {
    for(UserStatusType type : values()) {
      if(type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
  }
}
