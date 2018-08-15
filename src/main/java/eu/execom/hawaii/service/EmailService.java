package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
   * Send email.
   *
   * @param email given Email for send.
   */
  private void sendEmail(Email email) {
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

  void createEmailAndSendForApproval(Request request) {
    List<String> approversEmail = request.getUser()
                                         .getTeam()
                                         .getTeamApprovers()
                                         .stream()
                                         .map(User::getEmail)
                                         .collect(Collectors.toList());

    String subject = EmailFormatter.REQUEST_CREATED_SUBJECT;

    String requestUserName = request.getUser().getFullName();
    String requestAbsenceName = request.getAbsence().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String text = EmailFormatter.getRequestCreatedText(requestUserName, requestAbsenceName, startDate, endDate,
        numberOfRequestedDays, request.getReason());

    sendEmail(new Email(approversEmail, subject, text));
  }

  void createApprovedEmailAndSend(Request request) {
    List<String> userEmail = Arrays.asList(request.getUser().getEmail());
    String subject = "Request approved";
    String text = "Request for " + request.getAbsence().getName() + " is approved.";

    sendEmail(new Email(userEmail, subject, text));
  }
}
