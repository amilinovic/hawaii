package eu.execom.hawaii.model.batch;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserImport {

  private String firstName;
  private String lastName;
  private String email;
  private String payrollNo;
  private LocalDate continuousStartDate;
  private LocalDate startDate;
  private LocalDate endDate;
  private String jobTitle;
  private String status;
  private String team;
  private String virtualTeam;
  private String leaveProfile;

}
