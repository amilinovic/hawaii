package eu.execom.hawaii.converter;

import eu.execom.hawaii.model.enumerations.UserStatusType;

import java.beans.PropertyEditorSupport;

public class UserStatusTypeConverter extends PropertyEditorSupport {

  @Override
  public void setAsText(final String text) throws IllegalArgumentException {
    setValue(UserStatusType.valueOf(text));
  }
}
