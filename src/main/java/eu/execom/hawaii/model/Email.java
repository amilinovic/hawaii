package eu.execom.hawaii.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Email {

  private List<String> to;
  private String subject;
  private Map<String, Object> templateData;

}
