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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SendNotificationsService {
  private final ObjectMapper objectMapper;
  private final String authorizationKey;
  private final String firebaseApiUrl;

  public SendNotificationsService(@Value("${fcm.server-legacy-key}") String authorizationKey,
      @Value("${fcm.api-url}") String firebaseApiUrl, ObjectMapper objectMapper) {
    this.authorizationKey = authorizationKey;
    this.firebaseApiUrl = firebaseApiUrl;
    this.objectMapper = objectMapper;
  }

  public void sendNotificationToApproversAboutSubmittedRequest(Request newRequest) {
    PushNotificationToApproversDto result = new PushNotificationToApproversDto();
    NotificationDto notification = new NotificationDto();
    NotificationDataDto data = new NotificationDataDto();

    RequestStatus requestStatus = newRequest.getRequestStatus();

    switch (requestStatus) {
      case CANCELLATION_PENDING:
        notification.setTitle("New request!");
        notification.setBody(newRequest.getUser().getFullName() + "submitted new cancel request");
        notification.setPriority("high");
        data.setTitle("Data title");
        data.setBody("Data body");
        data.setPriority("high");
        data.setRequestStatus(newRequest.getRequestStatus());
        break;
      case PENDING:
        notification.setTitle("New request!");
        notification.setBody(newRequest.getUser().getFullName() + "submitted new leave request");
        notification.setPriority("high");
        data.setTitle("Data title");
        data.setBody("Data body");
        data.setPriority("high");
        data.setRequestStatus(newRequest.getRequestStatus());
        break;
      default:
        throw new IllegalArgumentException("Unsupported request status: " + newRequest.getRequestStatus());

    }
    notification.setTitle("New request!");
    notification.setBody(newRequest.getUser().getFullName().toString() + "submitted new request");
    notification.setPriority("high");
    data.setTitle("Data title");
    data.setBody("Data body");
    data.setPriority("high");
    data.setRequestStatus(newRequest.getRequestStatus());

    List<String> approversPushToken = newRequest.getUser()
                                                .getTeam()
                                                .getTeamApprovers()
                                                .stream()
                                                .map(User::getPushToken)
                                                .collect(Collectors.toList());
    result.setTo(approversPushToken);
    result.setNotification(notification);
    result.setData(data);
    String convertedToJson = objectToJsonMapper(result);
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

  private String objectToJsonMapper(Object result) {
    String jsonInString = "";

    try {
      jsonInString = objectMapper.writeValueAsString(result);
    } catch (IOException e) {
      log.error("Failed to send push notification {}", e);
    }
    return jsonInString;
  }

  private void send(String convertedToJson) {
    try {
      URL url = new URL(firebaseApiUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty("Authorization", "key=" + authorizationKey);
      conn.connect();

      OutputStream os = conn.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
      osw.write(convertedToJson);
      osw.flush();

      int responseCode = conn.getResponseCode();
      log.debug("Push returned code {}", responseCode);

    } catch (IOException e) {
      log.error("Failed to send push notification {}", e);
    }
  }
}
