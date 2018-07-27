package eu.execom.hawaii.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
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
  private static final String CREDENTIALS_FILE_PATH = "/service_account.json";

  private static final LocalTime MORNING_TIME = LocalTime.of(9, 0);
  private static final LocalTime MID_DAY = LocalTime.of(13, 0);
  private static final LocalTime AFTERNOON_TIME = LocalTime.of(17, 0);
  private static final String CALENDAR_ID = "primary";

  /**
   * Insert request to Google calendar.
   *
   * @param request Each day of the request is inserted individually either as an all-day event, or
   *                with start and end time depending on whether it is morning or afternoon only.
   * @throws Exception
   */
  public void insertRequestToCalendar(Request request) throws Exception {
    var calendarId = CALENDAR_ID;

    var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    var email = request.getUser().getEmail();
    var serviceAccountCredentials = new ClassPathResource(CREDENTIALS_FILE_PATH).getFile();
    var credentialsFromJson = GoogleCredential.fromStream(new FileInputStream(serviceAccountCredentials));
    var credential = new GoogleCredential.Builder().setTransport(httpTransport)
                                                   .setJsonFactory(JSON_FACTORY)
                                                   .setServiceAccountProjectId(
                                                       credentialsFromJson.getServiceAccountProjectId())
                                                   .setServiceAccountId(credentialsFromJson.getServiceAccountId())
                                                   .setServiceAccountPrivateKeyId(
                                                       credentialsFromJson.getServiceAccountPrivateKeyId())
                                                   .setServiceAccountPrivateKey(
                                                       credentialsFromJson.getServiceAccountPrivateKey())
                                                   .setServiceAccountScopes(SCOPES)
                                                   .setTokenServerEncodedUrl(
                                                       credentialsFromJson.getTokenServerEncodedUrl())
                                                   .setServiceAccountUser(email)
                                                   .build();
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
        start.setDate(new DateTime(true, date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(), null));
        end = start;
    }

    event.setStart(start);
    event.setEnd(end);
    event.setSummary("Approved leave: " + day.getRequest().getReason());
    event.setDescription(day.getRequest().getReason());

    return event;
  }

  private DateTime getDateTime(LocalDate date, LocalTime localTime) {
    return new DateTime(Date.from(Instant.from(date.atTime(localTime.atOffset(ZoneOffset.UTC)))),
        TimeZone.getDefault());
  }

}
