package eu.execom.hawaii.service;

import eu.execom.hawaii.configuration.HeaderRequestInterceptor;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class SendNotificationsService {
  private static final String FIREBASE_SERVER_KEY = "AAAA-fb15Pk:APA91bEPf55o8CrawoRDytyWSYuT4iHRW5yI-Pdvttes-gEUElDXSzbnyStZTRgOkyDvRl52Ls50dSFJNw3MPdalAdlFDNwFBIUHEqeNbrFJ6lpWHEjw8_B8Ue-NoQpUydZaCby9IE54";
  private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
  private final String TOPIC = "test";

  @Async
  public CompletableFuture<String> send(HttpEntity<String> entity) {
    RestTemplate restTemplate = new RestTemplate();

    ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
    interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
    restTemplate.setInterceptors(interceptors);

    String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
    return CompletableFuture.completedFuture(firebaseResponse);
  }

  public void sendNotificationForRequestedLeave(RequestStatus requestStatus, User authUser) {
    JSONObject body = new JSONObject();
    body.put("to", "/topics/" + TOPIC);
    body.put("register_ids", "[" + authUser.getEmail() + "]");
    body.put("token", authUser.getPushToken());

    JSONObject notification = new JSONObject();
    notification.put("Key-1", "JSA Data 1");
    notification.put("Key-2", "JSA Data 2");

    JSONObject data = new JSONObject();
    data.put("notification", notification);

    switch (requestStatus) {
      case APPROVED:
        body.put("Your request has been approved", requestStatus);
      case CANCELED:
        body.put("Your request has been cancelled", requestStatus);
      case REJECTED:
        body.put("Your request has been rejected", requestStatus);
    }

    HttpEntity<String> request = new HttpEntity<>(body.toString());

    CompletableFuture<String> pushNotification = send(request);
    CompletableFuture.allOf(pushNotification).join();

    try {
      String firebaseResponse = pushNotification.get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }
}
