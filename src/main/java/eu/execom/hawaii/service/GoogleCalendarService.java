package eu.execom.hawaii.service;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@Slf4j
public class GoogleCalendarService {

  private static final String APPLICATION_NAME = "hawaii";
  private static final String EVENT_ID_PREFIX = "event";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
  private static final String CREDENTIALS_FILE_PATH = "/service_account.json";

  private static final LocalTime MORNING_TIME = LocalTime.of(9, 0);
  private static final LocalTime MID_DAY = LocalTime.of(13, 0);
  private static final LocalTime AFTERNOON_TIME = LocalTime.of(17, 0);
  private static final String CALENDAR_ID = "primary";
  private static final String YELLOW_COLOR_ID = "5";
  private static final String GREEN_COLOR_ID = "2";

  private GoogleCredential googleCredential;

  @PostConstruct
  public void init() {
    try {
      var serviceAccountCredentials = new ClassPathResource(CREDENTIALS_FILE_PATH).getFile();
      googleCredential = GoogleCredential.fromStream(new FileInputStream(serviceAccountCredentials));
    } catch (IOException e) {
      log.error("Error reading credentials from file. Exception raised: {}", e);
    }
  }

  /**
   * Creates an event for each day of the request and inserts it into Google Calendar.
   *
   * @param request Each day of the request is inserted individually either as an all-day event, or
   *                with start and end time depending on whether it is morning or afternoon only.
   */
  void handleCreatedRequest(Request request) {

    Function<Day, Day> setRequest = day -> {
      day.setRequest(request);
      return day;
    };

    Consumer<Calendar> insertEvents = calendar -> request.getDays()
                                                         .stream()
                                                         .map(setRequest)
                                                         .map(this::createEvent)
                                                         .forEach(insertEventToCalendar(CALENDAR_ID, calendar));

    getCalendar(request).ifPresent(insertEvents);

  }

  /**
   * Handles update of the request status from PENDING to APPROVED or CANCELED.
   *
   * @param request         the request being updated.
   * @param requestCanceled true if request status is CANCELED in which case the existing event should be
   *                        removed from the calendar and false if event is APPROVED, in which case the
   *                        existing event should be updated with a new description and event color.
   */
  void handleRequestUpdate(Request request, boolean requestCanceled) {

    Consumer<Calendar> updateEvents = calendar -> getDayIdStream(request).forEach(
        updateEventOnApproval(CALENDAR_ID, calendar, request));

    Consumer<Calendar> removeEvents = calendar -> getDayIdStream(request).forEach(
        removeEventFromCalendar(CALENDAR_ID, calendar));

    getCalendar(request).ifPresent(requestCanceled ? removeEvents : updateEvents);

  }

  private Stream<String> getDayIdStream(Request request) {
    return request.getDays().stream().map(Day::getId).map(String::valueOf);
  }

  private Optional<Calendar> getCalendar(Request request) {
    Calendar calendar = null;
    try {
      var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      var email = request.getUser().getEmail();
      var credential = new GoogleCredential.Builder().setTransport(httpTransport)
                                                     .setJsonFactory(JSON_FACTORY)
                                                     .setServiceAccountProjectId(
                                                         googleCredential.getServiceAccountProjectId())
                                                     .setServiceAccountId(googleCredential.getServiceAccountId())
                                                     .setServiceAccountPrivateKeyId(
                                                         googleCredential.getServiceAccountPrivateKeyId())
                                                     .setServiceAccountPrivateKey(
                                                         googleCredential.getServiceAccountPrivateKey())
                                                     .setServiceAccountScopes(SCOPES)
                                                     .setTokenServerEncodedUrl(
                                                         googleCredential.getTokenServerEncodedUrl())
                                                     .setServiceAccountUser(email)
                                                     .build();
      calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
                                                                              .build();
    } catch (Exception e) {
      log.error("Error creating NetHttpTransport. Exception raised: {}", e);
    }
    return Optional.ofNullable(calendar);
  }

  private Consumer<Event> insertEventToCalendar(String calendarId, Calendar calendar) {
    return event -> {
      try {
        calendar.events().insert(calendarId, event).execute();
      } catch (IOException e) {
        log.error("Error adding event to Google calendar: ", e);
      }
    };
  }

  private Consumer<String> removeEventFromCalendar(String calendarId, Calendar calendar) {
    return dayId -> {
      try {
        var eventId = EVENT_ID_PREFIX + dayId;
        calendar.events().delete(calendarId, eventId).execute();
      } catch (IOException e) {
        log.error("Error removing event from Google calendar: ", e);
      }
    };
  }

  private Consumer<String> updateEventOnApproval(String calendarId, Calendar calendar, Request request) {
    return dayId -> {
      try {
        var eventId = EVENT_ID_PREFIX + dayId;
        var event = calendar.events().get(calendarId, eventId).execute();

        var summary = getSummary(request);
        event.setSummary(summary);
        event.setColorId(GREEN_COLOR_ID);

        calendar.events().patch(calendarId, eventId, event).execute();
      } catch (IOException e) {
        log.error("Error updating event from Google calendar: ", e);
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

    var request = day.getRequest();
    var summary = getSummary(request);
    event.setSummary(summary);
    event.setDescription(request.getReason());
    event.setId(EVENT_ID_PREFIX + String.valueOf(day.getId()));
    event.setColorId(YELLOW_COLOR_ID);

    return event;
  }

  private String getSummary(Request request) {
    var status = request.getRequestStatus().name().toLowerCase();
    var fullName = request.getUser().getFullName();
    return StringUtils.capitalize(status) + " leave: " + fullName;
  }

  private DateTime getDateTime(LocalDate date, LocalTime localTime) {
    var instant = LocalDateTime.of(date, localTime).toInstant(ZoneOffset.UTC);
    return new DateTime(Date.from(instant), TimeZone.getDefault());
  }

}
