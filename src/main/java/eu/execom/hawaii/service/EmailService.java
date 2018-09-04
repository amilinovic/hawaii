package eu.execom.hawaii.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import eu.execom.hawaii.model.Email;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.util.EmailFormatter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

  private static final String EMPTY_STRING = "";

  private JavaMailSender emailSender;

  private Configuration freemarkerConfig;

  @Autowired
  public EmailService(JavaMailSender emailSender, Configuration freemarkerConfig) {
    this.emailSender = emailSender;
    this.freemarkerConfig = freemarkerConfig;
  }

  /**
   * Create email on request creation and sends for approval to team approvers.
   *
   * @param request the Request.
   */
  @Async
  public void createEmailAndSendForApproval(Request request) {
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
    var reason = request.getReason();
    Map<String, Object> templateData = Map.of("userName", userName, "absenceName", absenceName, "startDate", startDate,
        "endDate", endDate, "numberOfRequestedDays", numberOfRequestedDays, "reason", reason);

    sendEmail(new Email(approversEmail, subject, templateData), "createRequestEmail");
  }

  /**
   * Create email after team approver action on given request and sends to request user.
   *
   * @param request the Request.
   */
  @Async
  public void createStatusNotificationEmailAndSend(Request request) {
    List<String> userEmail = Collections.singletonList(request.getUser().getEmail());
    String subject = EmailFormatter.getLeaveRequestNotificationSubject(request.getRequestStatus().toString());
    String userName = request.getUser().getFullName();
    String status = request.getRequestStatus().toString();
    String absenceName = request.getAbsence().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String reason = request.getReason();
    Map<String, Object> templateData = Map.of("userName", userName, "status", status, "absenceName", absenceName,
        "startDate", startDate, "endDate", endDate, "numberOfRequestedDays", numberOfRequestedDays, "reason", reason);

    sendEmail(new Email(userEmail, subject, templateData), "requestNotificationEmail");
  }

  /**
   * Create email on user submitting sickness request and sends to user teammates.
   *
   * @param request the Request.
   */
  @Async
  public void createSicknessEmailForTeammatesAndSend(Request request) {
    var userEmail = request.getUser().getEmail();
    List<String> teammatesEmails = request.getUser()
                                          .getTeam()
                                          .getUsers()
                                          .stream()
                                          .map(User::getEmail)
                                          .filter(isTeammateEmail(userEmail))
                                          .collect(Collectors.toList());
    String subject = EmailFormatter.TEAM_NOTIFICATION_SICKNESS_SUBJECT;
    String userName = request.getUser().getFullName();
    String teamName = request.getUser().getTeam().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String reason = request.getAbsence().getName();
    Map<String, Object> templateData = Map.of("userName", userName, "teamName", teamName, "startDate", startDate,
        "endDate", endDate, "numberOfRequestedDays", numberOfRequestedDays, "reason", reason);

    sendEmail(new Email(teammatesEmails, subject, templateData), "teammatesSicknessEmail");
  }

  /**
   * Create email on user approved annual leave request and sends to user teammates.
   *
   * @param request the Request.
   */
  @Async
  public void createAnnualEmailForTeammatesAndSend(Request request) {
    var userEmail = request.getUser().getEmail();
    List<String> teammatesEmails = request.getUser()
                                          .getTeam()
                                          .getUsers()
                                          .stream()
                                          .map(User::getEmail)
                                          .filter(isTeammateEmail(userEmail))
                                          .collect(Collectors.toList());
    String subject = EmailFormatter.TEAM_NOTIFICATION_ANNUAL_SUBJECT;
    String userName = request.getUser().getFullName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String teamName = request.getUser().getTeam().getName();
    Map<String, Object> templateData = Map.of("userName", userName, "numberOfRequestedDays", numberOfRequestedDays,
        "startDate", startDate, "endDate", endDate, "teamName", teamName);

    sendEmail(new Email(teammatesEmails, subject, templateData), "teammatesAnnualEmail");
  }

  private Predicate<String> isTeammateEmail(String requestUserEmail) {
    return email -> !requestUserEmail.equals(email);
  }

  private void sendEmail(Email email, String templateName) {
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    Template template = getTemplate(templateName);

    var emailText = processTemplateIntoString(template, email.getTemplateData());
    createEmail(email, helper, emailText);

    try {
      emailSender.send(message);
    } catch (MailException exception) {
      log.error("Error on sending email to: {}", email.getTo());
    }
  }

  private Template getTemplate(String templateName) {
    Template template = null;
    try {
      template = freemarkerConfig.getTemplate(templateName);
    } catch (IOException exception) {
      log.error("Unable to find template with name: {}", templateName);
    }
    return template;
  }

  private void createEmail(Email email, MimeMessageHelper helper, String emailText) {
    var listSize = email.getTo().size();
    try {
      helper.setTo(email.getTo().toArray(new String[listSize]));
      helper.setSubject(email.getSubject());
      helper.getMimeMessage().setContent(emailText, "text/html;charset=utf-8");
    } catch (MessagingException exception) {
      log.error("Error creating email with details: {}", exception);
    }
  }

  private String processTemplateIntoString(Template template, Map<String, Object> templateData) {
    String emailText = EMPTY_STRING;
    try {
      emailText = FreeMarkerTemplateUtils.processTemplateIntoString(Objects.requireNonNull(template), templateData);
    } catch (IOException | TemplateException exception) {
      log.error("Error processing template to string with details: {}", exception);
    }
    return emailText;
  }

}
