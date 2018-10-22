package eu.execom.hawaii.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.execom.hawaii.dto.NotificationDataDto;
import eu.execom.hawaii.dto.NotificationDto;
import eu.execom.hawaii.dto.PushNotificationDto;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class SendNotificationsService {
  private static final Logger logger = LoggerFactory.getLogger(SendNotificationsService.class);
  private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

  @Async
  public void send(String convertedToJson) {
    BufferedReader in = null;
    OutputStream os = null;

    try {
      URL url = new URL(FIREBASE_API_URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty("Authorization", "key=AIzaSyCHQvWMzrWEbD2NBv4ZzVsAUr2KZx5W1og");
      conn.connect();

      os = conn.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
      osw.write(convertedToJson);
      osw.flush();
      osw.close();

      int responseCode = conn.getResponseCode();
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      logger.debug("Push returned code {}", responseCode);
    } catch (IOException e){
      logger.error("Failed to send push notification {}", e);
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(os);
    }
  }

  public void sendNotificationForRequestedLeave(RequestStatus requestStatus, User user) {
    PushNotificationDto result = new PushNotificationDto();
    NotificationDto notification = new NotificationDto();
    NotificationDataDto data = new NotificationDataDto();

    switch (requestStatus) {
      case APPROVED:
        notification.setTitle("Approved!");
        notification.setBody("Your request has been approved");
        notification.setPriority("high");
        data.setTitle("Data title");
        data.setBody("Data body");
        data.setPriority("high");
        data.setRequestStatus(RequestStatus.APPROVED);
        break;
      case CANCELED:
        notification.setTitle("Cancelled!");
        notification.setBody("Your request has been cancelled");
        notification.setPriority("high");
        data.setTitle("Data title");
        data.setBody("Data body");
        data.setPriority("high");
        data.setRequestStatus(RequestStatus.CANCELED);
        break;
      case REJECTED:
        notification.setTitle("Rejected!");
        notification.setBody("Your request has been rejected");
        notification.setPriority("high");
        data.setTitle("Data title");
        data.setBody("Data body");
        data.setPriority("high");
        data.setRequestStatus(RequestStatus.REJECTED);
        break;
    }

    result.setTo(user.getPushToken());
    result.setNotification(notification);
    result.setData(data);

    String convertedToJson = objectToJsonMapper(result);
    send(convertedToJson);
  }

  private String objectToJsonMapper(PushNotificationDto result) {
    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = "";

    try {
      jsonInString = mapper.writeValueAsString(result);
    } catch (IOException e) {
      logger.error("Failed to send push notification {}", e);
    }
    return jsonInString;
  }
}
