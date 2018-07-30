package eu.execom.hawaii.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
   * Handles adding and removing events from Google calendar.
   *
   * @param request Each day of the request is inserted individually either as an all-day event, or
   *                with start and end time depending on whether it is morning or afternoon only.
   * @param apply true if request status is APPROVED, in which case a new event should be inserted into
   *              the calendar and false if event is CANCELLED in which case the existing event should be
   *              removed from the calendar.
   */
  void handleRequest(Request request, boolean apply) {

    Consumer<Calendar> insertEvents = calendar -> request.getDays()
                                                         .stream()
                                                         .map(this::createEvent)
                                                         .forEach(insertEventToCalendar(CALENDAR_ID, calendar));

    Consumer<Calendar> removeEvents = calendar -> request.getDays()
                                                         .stream()
                                                         .map(Day::getId)
                                                         .map(String::valueOf)
                                                         .forEach(removeEventFromCalendar(CALENDAR_ID, calendar));

    getCalendar(request).ifPresent(apply ? insertEvents : removeEvents);

  }

  private Optional<Calendar> getCalendar(Request request) {
    Calendar calendar = null;
    try {
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
      calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
                                                                              .build();
    } catch (GeneralSecurityException e) {
      log.error("Error creating NetHttpTransport. Exception raised: {}", e);
    } catch (IOException e) {
      log.error("Error reading credentials from file. Exception raised: {}", e);
    }
    return Optional.ofNullable(calendar);
  }

  private Consumer<Event> insertEventToCalendar(String calendarId, Calendar service) {
    return event -> {
      try {
        service.events().insert(calendarId, event).execute();
      } catch (IOException e) {
        log.error("Error adding event to Google calendar: ", e);
      }
    };
  }

  private Consumer<String> removeEventFromCalendar(String calendarId, Calendar service) {
    return eventId -> {
      try {
        service.events().delete(calendarId, eventId).execute();
      } catch (IOException e) {
        log.error("Error removing event from Google calendar: ", e);
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
        var epochMilli = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        start.setDate(new DateTime(true, epochMilli, null));
        end = start;
    }

    event.setStart(start);
    event.setEnd(end);
    event.setSummary("Approved leave: " + day.getRequest().getReason());
    event.setId(String.valueOf(day.getId()));

    return event;
  }

  private DateTime getDateTime(LocalDate date, LocalTime localTime) {
    var instant = LocalDateTime.of(date, localTime).toInstant(ZoneOffset.UTC);
    return new DateTime(Date.from(instant), TimeZone.getDefault());
  }

}
