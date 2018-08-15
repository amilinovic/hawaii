package eu.execom.hawaii.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.Email;
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
  public void sendEmail(Email email) {
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
