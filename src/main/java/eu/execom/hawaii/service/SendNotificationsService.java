package eu.execom.hawaii.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.execom.hawaii.dto.NotificationDataDto;
import eu.execom.hawaii.dto.NotificationDto;
import eu.execom.hawaii.dto.PushNotificationDto;
import eu.execom.hawaii.dto.PushNotificationToApproversDto;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SendNotificationsService {
  private static final Logger logger = LoggerFactory.getLogger(SendNotificationsService.class);
  private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

  private final ObjectMapper objectMapper;
  private final String authorizationKey;

  public SendNotificationsService(@Value("${fcm.server-legacy-key}") String authorizationKey, ObjectMapper objectMapper) {
    this.authorizationKey = authorizationKey;
    this.objectMapper = objectMapper;
  }

  public void sendNotificationToApproversAboutSubmittedRequest(Request newRequest) {
    PushNotificationToApproversDto result = new PushNotificationToApproversDto();
    NotificationDto notification = new NotificationDto();
    NotificationDataDto data = new NotificationDataDto();

    notification.setTitle("New request!");
    notification.setBody("User submitted new leave request");
    notification.setPriority("high");
    data.setTitle("Data title");
    data.setBody("Data body");
    data.setPriority("high");
    data.setRequestStatus(RequestStatus.PENDING);

    List<String> approversPushToken = newRequest.getUser()
                                         .getTeam()
                                         .getTeamApprovers()
                                         .stream()
                                         .map(User::getPushToken)
                                         .collect(Collectors.toList());
      result.setTo(approversPushToken);
      result.setNotification(notification);
      result.setData(data);
      String convertedToJson = objectToJsonMapperList(result);
      send(convertedToJson);
  }

  public void sendNotificationForRequestedLeave(RequestStatus requestStatus, User user) {
    PushNotificationDto result = new PushNotificationDto();
    NotificationDto notification = new NotificationDto();
    NotificationDataDto data = new NotificationDataDto();

    notification.setPriority("high");
    data.setTitle("Data title");
    data.setBody("Data body");
    data.setPriority("high");
    data.setRequestStatus(requestStatus);

    switch (requestStatus) {
      case APPROVED:
        notification.setTitle("Approved!");
        notification.setBody("Your request has been approved");
        break;
      case CANCELED:
        notification.setTitle("Cancelled!");
        notification.setBody("Your request has been cancelled");
        break;
      case REJECTED:
        notification.setTitle("Rejected!");
        notification.setBody("Your request has been rejected");
        break;
      default:
        throw new IllegalArgumentException("Unsupported request status: " + requestStatus);
    }

    result.setTo(user.getPushToken());
    result.setNotification(notification);
    result.setData(data);

    String convertedToJson = objectToJsonMapper(result);
    send(convertedToJson);
  }

  private String objectToJsonMapper(PushNotificationDto result) {
    String jsonInString = "";

    try {
      jsonInString = objectMapper.writeValueAsString(result);
    } catch (IOException e) {
      logger.error("Failed to send push notification {}", e);
    }
    return jsonInString;
  }

  private String objectToJsonMapperList(PushNotificationToApproversDto result) {
    String jsonInString = "";

    try {
      jsonInString = objectMapper.writeValueAsString(result);
    } catch (IOException e) {
      logger.error("Failed to send push notification {}", e);
    }
    return jsonInString;
  }

  @Async
  private void send(String convertedToJson) {
    try {
      URL url = new URL(FIREBASE_API_URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty("Authorization", "key=" + authorizationKey);
      conn.connect();

      int responseCode = conn.getResponseCode();
      logger.debug("Push returned code {}", responseCode);

      try (OutputStream os = conn.getOutputStream();
          OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
          BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        osw.write(convertedToJson);
        osw.flush();

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }
    } catch (IOException e) {
      logger.error("Failed to send push notification {}", e);
    }
  }
}
