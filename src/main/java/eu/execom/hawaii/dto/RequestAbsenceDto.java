package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.Request;
import lombok.Data;

@Data
public class RequestAbsenceDto {
  private RequestDto requestDto;
  private AbsenceDto absenceDto;

  public RequestAbsenceDto(Request request, Absence absence) {
    this.requestDto = new RequestDto(request);
    this.absenceDto = new AbsenceDto(absence);
  }
}
