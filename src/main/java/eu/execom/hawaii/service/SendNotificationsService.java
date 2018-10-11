package eu.execom.hawaii.service;

import eu.execom.hawaii.model.FcmClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class SendNotificationsService {
  private final FcmClient fcmClient;

  private int seq = 0;

  public SendNotificationsService(FcmClient fcmClient) {
    this.fcmClient = fcmClient;
  }

  @Scheduled(fixedDelay = 30_000)
  public void sendNotifications() {
    String notification = "notification";
    try {
      sendPushMessages(notification);
    }
    catch (InterruptedException | ExecutionException e) {
      Application.logger.error("send notification", e);
    }
  }

}
