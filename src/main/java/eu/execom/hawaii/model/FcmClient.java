package eu.execom.hawaii.model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import eu.execom.hawaii.configuration.FcmSettings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FcmClient {

  public FcmClient(FcmSettings settings) {
    Path p = Paths.get(settings.getServiceAccountFile());
    try (InputStream serviceAccount = Files.newInputStream(p)) {
      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

      FirebaseApp.initializeApp(options);
    }
    catch (IOException e) {
      Application.logger.error("init fcm", e);
    }
  }

  public void send(Map<String, String> data)
      throws InterruptedException, ExecutionException {

    Message message = Message.builder().putAllData(data).setTopic("notification")
                             .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
                                                                      .setNotification(new WebpushNotification("Background Title (server)",
                                                                "Background Body (server)", "mail2.png"))
                                                                      .build())
                             .build();

    String response = FirebaseMessaging.getInstance().sendAsync(message).get();
    System.out.println("Sent message: " + response);
  }

}