package eu.execom.hawaii.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;

@Data
public class AbsenceDto {

  private Long id;
  private AbsenceType absenceType;
  private String name;
  private String comment;
  private boolean active;
  private List<RequestDto> leaveRequests = new ArrayList<>();

  public AbsenceDto(Absence absence) {
    this.id = absence.getId();
    this.name = absence.getName();
    this.comment = absence.getComment();
    this.active = absence.isActive();
    this.absenceType = absence.getAbsenceType();
    this.leaveRequests = absence.getLeaveRequests().stream().map(RequestDto::new).collect(Collectors.toList());
  }

}
