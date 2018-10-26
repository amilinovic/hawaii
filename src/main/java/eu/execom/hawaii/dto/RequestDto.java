package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RequestDto {

  private Long id;
  private UserDto user;
  private AbsenceDto absence;
  private RequestStatus requestStatus;
  private String reason;
  private LocalDateTime submissionTime;
  @NotNull
  private List<DayDto> days;

  public RequestDto(Request request) {
    this.id = request.getId();
    this.user = new UserDto(request.getUser());
    this.absence = new AbsenceDto(request.getAbsence());
    this.requestStatus = request.getRequestStatus();
    this.reason = request.getReason();
    this.submissionTime = request.getSubmissionTime();
    this.days = request.getDays().stream().map(DayDto::new).collect(Collectors.toList());
  }

}