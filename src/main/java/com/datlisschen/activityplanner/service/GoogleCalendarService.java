package com.datlisschen.activityplanner.service;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleCalendarService {

    @Value("${google.calendar.id}")
    private String calendarId;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private Calendar getCalendarService() throws IOException, GeneralSecurityException {
        InputStream in = GoogleCalendarService.class.getResourceAsStream("/service-account-key.json");
        if (in == null) {
            throw new IOException("Service account key file not found in resources!");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR_EVENTS));

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName("Activity Planner")
                .build();
    }
    public String addIdeaToCalendar(ActivityIdea idea) {
        if (idea.getStartTime() == null || idea.getEndTime() == null) return null;

        try {
            Calendar service = getCalendarService();

            Event event = new Event()
                    .setSummary(idea.getTitle())
                    .setLocation(idea.getPlace() != null ? idea.getPlace() : "")
                    .setDescription(idea.getDescription());

            // SAFE CONVERSION: LocalDateTime -> ZonedDateTime -> Instant -> java.util.Date
            java.util.Date startDate = java.util.Date.from(idea.getStartTime()
                    .atZone(java.time.ZoneId.systemDefault()).toInstant());
            java.util.Date endDate = java.util.Date.from(idea.getEndTime()
                    .atZone(java.time.ZoneId.systemDefault()).toInstant());

            // Create Google DateTime objects from the java.util.Date
            event.setStart(new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(startDate)));
            event.setEnd(new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(endDate)));

            // If updating an existing event, use update() instead of insert()
            Event result;
            if (idea.getGoogleEventId() != null && !idea.getGoogleEventId().isEmpty()) {
                result = service.events().update(calendarId, idea.getGoogleEventId(), event).execute();
            } else {
                result = service.events().insert(calendarId, event).execute();
            }

            return result.getId();

        } catch (Exception e) {
            System.err.println("Google Sync Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void deleteEvent(String eventId) {
        if (eventId == null || eventId.isEmpty()) return;
        try {
            Calendar service = getCalendarService();
            service.events().delete(calendarId, eventId).execute();
            System.out.println("Google Event deleted: " + eventId);
        } catch (Exception e) {
            System.err.println("Could not delete Google Event: " + e.getMessage());
        }
    }
}