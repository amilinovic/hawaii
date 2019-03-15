package eu.execom.hawaii.configuration;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BeanWrapperFieldSetMapperCustom<T> extends BeanWrapperFieldSetMapper<T> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Override
  protected void initBinder(DataBinder binder) {
    binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {

      @Override
      public String getAsText() throws IllegalArgumentException {
        Object date = getValue();
        if (date != null) {
          return FORMATTER.format((LocalDate) date);
        } else {
          return "";
        }
      }

      @Override
      public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.isEmpty(text)) {
          setValue(LocalDate.parse(text, FORMATTER));
        } else {
          setValue(null);
        }
      }
    });
  }

}
