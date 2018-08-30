package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.Email;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.util.EmailFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

  private JavaMailSender emailSender;

  @Autowired
  public EmailService(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  /**
   * Create email on request creation and sends for approval to team approvers.
   *
   * @param request the Request.
   */
  void createEmailAndSendForApproval(Request request) {
    List<String> approversEmail = request.getUser()
                                         .getTeam()
                                         .getTeamApprovers()
                                         .stream()
                                         .map(User::getEmail)
                                         .collect(Collectors.toList());

    String subject = EmailFormatter.REQUEST_CREATED_SUBJECT;

    String userName = request.getUser().getFullName();
    String absenceName = request.getAbsence().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String text = EmailFormatter.getRequestCreatedText(userName, absenceName, startDate, endDate,
        numberOfRequestedDays, request.getReason());

    sendEmail(new Email(approversEmail, subject, text));
  }

  /**
   * Create email after team approver action on given request and sends to request user.
   *
   * @param request the Request.
   */
  void createStatusNotificationEmailAndSend(Request request) {
    List<String> userEmail = Collections.singletonList(request.getUser().getEmail());
    String subject = EmailFormatter.getLeaveRequestNotificationSubject(request.getRequestStatus().toString());
    String userName = request.getUser().getFullName();
    String status = request.getRequestStatus().toString();
    String absenceName = request.getAbsence().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String reason = request.getReason();
    String text = EmailFormatter.getLeaveRequestNotificationText(userName, status, absenceName, startDate, endDate,
        numberOfRequestedDays, reason);

    sendEmail(new Email(userEmail, subject, text));
  }

  /**
   * Create email on user submitting sickness request and sends to user teammates.
   *
   * @param request the Request.
   */
  void createSicknessEmailForTeammatesAndSend(Request request) {
    List<String> teammatesEmails = request.getUser()
                                          .getTeam()
                                          .getUsers()
                                          .stream()
                                          .map(User::getEmail)
                                          .collect(Collectors.toList());
    String subject = EmailFormatter.TEAM_NOTIFICATION_SICKNESS_SUBJECT;
    String userName = request.getUser().getFullName();
    String teamName = request.getUser().getTeam().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String reason = request.getAbsence().getName();
    String text = EmailFormatter.getTeamNotificationSicknessText(userName, teamName, startDate, endDate,
        numberOfRequestedDays, reason);

    sendEmail(new Email(teammatesEmails, subject, text));
  }

  /**
   * Create email on user approved annual leave request and sends to user teammates.
   *
   * @param request the Request.
   */
  void createAnnualEmailForTeammatesAndSend(Request request) {
    List<String> teammatesEmails = request.getUser()
                                          .getTeam()
                                          .getUsers()
                                          .stream()
                                          .map(User::getEmail)
                                          .collect(Collectors.toList());
    String subject = EmailFormatter.TEAM_NOTIFICATION_ANNUAL_SUBJECT;
    String userName = request.getUser().getFullName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String teamName = request.getUser().getTeam().getName();
    String text = EmailFormatter.getTeamNotificationAnnualText(userName, numberOfRequestedDays, startDate, endDate,
        teamName);

    sendEmail(new Email(teammatesEmails, subject, text));
  }

  @Async
  void sendEmail(Email email) {
    SimpleMailMessage message = new SimpleMailMessage();
    var listSize = email.getTo().size();
    message.setTo(email.getTo().toArray(new String[listSize]));
    message.setSubject(email.getSubject());
    message.setText(email.getText());

    try {
      emailSender.send(message);
    } catch (MailException exception) {
      log.error("Error on sending email to: {}", email.getTo());
    }
  }

}
