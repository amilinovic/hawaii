package eu.execom.hawaii.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleCalendarService {

  private static final String APPLICATION_NAME = "Hawaii";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
  private static final String CREDENTIALS_FOLDER = "credentials";
  private static final String CLIENT_SECRET_DIR = "client_secret.json";

  private static final LocalTime MORNING_TIME = LocalTime.of(9, 0);
  private static final LocalTime MID_DAY = LocalTime.of(13, 0);
  private static final LocalTime AFTERNOON_TIME = LocalTime.of(17, 0);

  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, Request request) throws Exception {
    // Load client secrets.
    var in = GoogleCalendarService.class.getResourceAsStream(CLIENT_SECRET_DIR);
    var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    var flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
        SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
               .setAccessType("offline")
               .build();
    var email = request.getUser().getEmail();
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(email);
  }

  /**
   * Insert request to Google calendar.
   *
   * @param request Each day of the request is inserted individually either as an all-day event, or
   *                with start and end time depending on whether it is morning or afternoon only.
   * @throws Exception
   */
  public void insertRequestToCalendar(Request request) throws Exception {
    var calendarId = "primary";
    var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    var credential = getCredentials(httpTransport, request);

    var service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
                                                                               .build();
    request.getDays().stream().map(this::createEvent).forEach(insertEventToCalendar(calendarId, service));
  }

  private Consumer<Event> insertEventToCalendar(String calendarId, Calendar service) {
    return event -> {
      try {
        service.events().insert(calendarId, event).execute();
      } catch (IOException e) {
        log.error("Error trying to contact Google calendar service: ", e);
      }
    };
  }

  private Event createEvent(Day day) {

    var event = new Event();

    var date = day.getDate();
    var start = new EventDateTime();
    var end = new EventDateTime();

    switch (day.getDuration()) {
      case MORNING:
        start.setDateTime(getDateTime(date, MORNING_TIME));
        end.setDateTime(getDateTime(date, MID_DAY));
        break;
      case AFTERNOON:
        start.setDateTime(getDateTime(date, MID_DAY));
        end.setDateTime(getDateTime(date, AFTERNOON_TIME));
        break;
      default:
        start.setDate(getDateTime(date, MORNING_TIME));
        end = start;
    }

    event.setStart(start);
    event.setEnd(end);
    event.setSummary("Approved leave");
    event.setDescription(day.getRequest().getReason());

    return event;
  }

  private DateTime getDateTime(LocalDate date, LocalTime morningTime) {
    return new DateTime(Date.from(Instant.from(date.atTime(morningTime))), TimeZone.getDefault());
  }

}
