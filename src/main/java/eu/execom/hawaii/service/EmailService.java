package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Email;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.util.EmailSubjectProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailService {

  private static final String EMPTY_STRING = "";
  private static final String USER_NAME = "userName";
  private static final String ABSENCE_NAME = "absenceName";
  private static final String START_DATE = "startDate";
  private static final String END_DATE = "endDate";
  private static final String NUMBER_OF_REQUESTED_DAYS = "numberOfRequestedDays";
  private static final String REASON = "reason";
  private static final String TEAM_NAME = "teamName";
  private static final String STATUS = "status";

  private JavaMailSender emailSender;

  private Configuration freemarkerConfig;

  private UserRepository userRepository;

  @Autowired
  public EmailService(JavaMailSender emailSender, Configuration freemarkerConfig, UserRepository userRepository) {
    this.emailSender = emailSender;
    this.freemarkerConfig = freemarkerConfig;
    this.userRepository = userRepository;
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

    String subject = EmailSubjectProvider.REQUEST_CREATED_SUBJECT;

    String userName = request.getUser().getFullName();
    String absenceName = request.getAbsence().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    var reason = request.getReason();
    Map<String, Object> templateData = Map.of(USER_NAME, userName, ABSENCE_NAME, absenceName, START_DATE, startDate,
        END_DATE, endDate, NUMBER_OF_REQUESTED_DAYS, numberOfRequestedDays, REASON, reason);

    sendEmail(new Email(approversEmail, subject, templateData), "createRequestEmail.ftl");
  }

  /**
   * Create email to send notification that user's leave profile was updated
   *
   * @param user the User.
   */
  @Async
  public void createLeaveProfileUpdateEmailAndSendForApproval(User user) {
    List<User> users = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));
    List<String> approversEmail = users.stream()
                                       .filter(approvers -> approvers.getUserRole().equals(UserRole.HR_MANAGER))
                                       .map(User::getEmail)
                                       .collect(Collectors.toList());

    String subject = EmailSubjectProvider.USER_LEAVE_PROFILE_UPDATE;
    String userName = user.getFullName();
    Map<String, Object> templateData = Map.of(USER_NAME, userName);

    sendEmail(new Email(approversEmail, subject, templateData), "userLeaveProfileUpdate.ftl");
  }

  /**
   * Create email after team approver action on given request and sends to request user.
   *
   * @param request the Request.
   */
  @Async
  public void createStatusNotificationEmailAndSend(Request request) {
    List<String> userEmail = Collections.singletonList(request.getUser().getEmail());
    String subject = EmailSubjectProvider.getLeaveRequestNotificationSubject(request.getRequestStatus().toString());
    String userName = request.getUser().getFullName();
    String status = request.getRequestStatus().toString();
    String absenceName = request.getAbsence().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String reason = request.getReason();
    Map<String, Object> templateData = Map.of(USER_NAME, userName, STATUS, status, ABSENCE_NAME, absenceName,
        START_DATE, startDate, END_DATE, endDate, NUMBER_OF_REQUESTED_DAYS, numberOfRequestedDays, REASON, reason);

    sendEmail(new Email(userEmail, subject, templateData), "requestNotificationEmail.ftl");
  }

  /**
   * Create email on user submitting sickness request and send to recipients for sickness request.
   *
   * @param request the Request.
   */
  @Async
  public void createSicknessEmailForNotifiersAndSend(Request request) {
    List<String> recipients = getRecipientsEmails(request.getUser().getTeam().getSicknessRequestEmails());
    if (request.getUser().getTeam().isSendEmailToTeammatesForSicknessRequestEnabled()) {
      recipients.addAll(getTeammatesEmailAddresses(request));
    }

    collectEmailData(EmailSubjectProvider.SICKNESS_SUBJECT, "recipientsSicknessEmail.ftl", recipients, request);
  }

  /**
   * Create email on user approved annual leave request and send to recipients for annual request.
   *
   * @param request the Request.
   */
  @Async
  public void createAnnualEmailForNotifiersAndSend(Request request) {
    List<String> recipients = getRecipientsEmails(request.getUser().getTeam().getAnnualRequestEmails());
    if (request.getUser().getTeam().isSendEmailToTeammatesForAnnualRequestEnabled()) {
      recipients.addAll(getTeammatesEmailAddresses(request));
    }

    collectEmailData(EmailSubjectProvider.ANNUAL_SUBJECT, "recipientsAnnualEmail.ftl", recipients, request);
  }

  /**
   * Create email on user approved bonus leave request and send to recipients for bonus request.
   *
   * @param request the Request.
   */
  @Async
  public void createBonusEmailForNotifiersAndSend(Request request) {
    List<String> recipients = getRecipientsEmails(request.getUser().getTeam().getBonusRequestEmails());
    if (request.getUser().getTeam().isSendEmailToTeammatesForBonusRequestEnabled()) {
      recipients.addAll(getTeammatesEmailAddresses(request));
    }

    collectEmailData(EmailSubjectProvider.BONUS_SUBJECT, "recipientsBonusEmail.ftl", recipients, request);
  }

  private List<String> getRecipientsEmails(String emails) {
    return Arrays.asList(emails.split("\\s*,\\s*"));
  }

  private List<String> getTeammatesEmailAddresses(Request request) {
    var userEmail = request.getUser().getEmail();
    return request.getUser()
                  .getTeam()
                  .getUsers()
                  .stream()
                  .map(User::getEmail)
                  .filter(isTeammateEmail(userEmail))
                  .collect(Collectors.toList());
  }

  private Predicate<String> isTeammateEmail(String requestUserEmail) {
    return email -> !requestUserEmail.equals(email);
  }

  private void collectEmailData(String subject, String emailTemplate, List<String> recipients, Request request) {
    Map<String, Object> templateData = collectTemplateData(request);

    Email email = new Email(recipients, subject, templateData);
    sendEmail(email, emailTemplate);
  }

  private Map<String, Object> collectTemplateData(Request request) {
    String userName = request.getUser().getFullName();
    String teamName = request.getUser().getTeam().getName();
    int numberOfRequestedDays = request.getDays().size();
    LocalDate startDate = request.getDays().get(0).getDate();
    LocalDate endDate = request.getDays().get(numberOfRequestedDays - 1).getDate();
    String reason = request.getAbsence() == null ? EMPTY_STRING : request.getAbsence().getName();
    return Map.of(USER_NAME, userName, TEAM_NAME, teamName, NUMBER_OF_REQUESTED_DAYS, numberOfRequestedDays, START_DATE,
        startDate, END_DATE, endDate, REASON, reason);
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
      log.error("Error on sending email to: " + email.getTo(), exception);
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
