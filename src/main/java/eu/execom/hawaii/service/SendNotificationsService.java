package eu.execom.hawaii.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.execom.hawaii.dto.NotificationDataDto;
import eu.execom.hawaii.dto.NotificationDto;
import eu.execom.hawaii.dto.PushNotificationDto;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.enumerations.Platform;
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
import java.util.Map;
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
    PushNotificationDto result = new PushNotificationDto();
    NotificationDto notification = new NotificationDto();
    NotificationDataDto data = new NotificationDataDto();

    String bodyMessage;
    RequestStatus requestStatus = newRequest.getRequestStatus();

    switch (requestStatus) {
      case CANCELLATION_PENDING:
        bodyMessage = newRequest.getUser().getFullName() + " submitted new cancel request";
        break;
      case PENDING:
        bodyMessage = newRequest.getUser().getFullName() + " submitted new leave request";
        break;
      default:
        throw new IllegalArgumentException("Unsupported request status: " + requestStatus);

    }
    notification.setTitle("New request!");
    notification.setBody(bodyMessage);
    notification.setPriority("high");
    notification.setClickAction("requestNotification");
    data.setTitle("New request!");
    data.setBody(bodyMessage);
    data.setPriority("high");
    data.setRequestStatus(requestStatus);
    data.setRequestId((newRequest.getId()).intValue());

    List<User> approvers = newRequest.getUser().getTeam().getTeamApprovers();
    for (User u : approvers) {
      Map<Platform, List<String>> pushTokensPerPlatform = u.getUserPushTokens()
                                                           .stream()
                                                           .collect(Collectors.groupingBy(UserPushToken::getPlatform,
                                                               Collectors.mapping(UserPushToken::getPushToken,
                                                                   Collectors.toList())));
      pushTokensPerPlatform.forEach((platform, pushTokens) -> {
        result.setTo(pushTokens);
        result.setData(data);

        switch (platform) {
          case IOS:
            result.setNotification(notification);
            result.setMutableContent(true);
            break;
          case ANDROID:
            result.setNotification(null);
            result.setMutableContent(false);
            break;
          default:
            throw new IllegalArgumentException("Unsupported platform " + platform);
        }

        String convertedToJson = objectToJsonMapper(result);
        send(convertedToJson);
      });
    }
  }

  public void sendNotificationForRequestedLeave(RequestStatus requestStatus, User user) {
    PushNotificationDto result = new PushNotificationDto();
    NotificationDto notification = new NotificationDto();
    NotificationDataDto data = new NotificationDataDto();

    notification.setPriority("high");
    notification.setClickAction("requestNotification");
    data.setPriority("high");
    data.setRequestStatus(requestStatus);

    switch (requestStatus) {
      case APPROVED:
        notification.setTitle("Approved!");
        notification.setBody("Your request has been approved");
        data.setTitle("Approved!");
        data.setBody("Your request has been approved");
        break;
      case CANCELED:
        notification.setTitle("Cancelled!");
        notification.setBody("Your request has been cancelled");
        data.setTitle("Cancelled!");
        data.setBody("Your request has been cancelled");
        break;
      case REJECTED:
        notification.setTitle("Rejected!");
        notification.setBody("Your request has been rejected");
        data.setTitle("Rejected!");
        data.setBody("Your request has been rejected");
        break;
      default:
        throw new IllegalArgumentException("Unsupported request status: " + requestStatus);
    }

    Map<Platform, List<String>> pushTokensPerPlatform = user.getUserPushTokens()
                                                            .stream()
                                                            .collect(Collectors.groupingBy(UserPushToken::getPlatform,
                                                                Collectors.mapping(UserPushToken::getPushToken,
                                                                    Collectors.toList())));

    pushTokensPerPlatform.forEach((platform, pushTokens) -> {
      result.setTo(pushTokens);
      result.setData(data);

      switch (platform) {
        case IOS:
          result.setNotification(notification);
          result.setMutableContent(true);
          break;
        case ANDROID:
          result.setNotification(null);
          result.setMutableContent(false);
          break;
        default:
          throw new IllegalArgumentException("Unsupported platform " + platform);
      }

      String convertedToJson = objectToJsonMapper(result);
      send(convertedToJson);
    });
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

      try (OutputStream os = conn.getOutputStream();
          OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        osw.write(convertedToJson);
        osw.flush();
      }

      int responseCode = conn.getResponseCode();
      log.debug("Push returned code {}", responseCode);

    } catch (IOException e) {
      log.error("Failed to send push notification {}", e);
    }
  }
}
