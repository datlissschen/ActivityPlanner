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

    public void addIdeaToCalendar(ActivityIdea idea) {
        if (idea.getPreciseDate() == null) return;

        try {
            Calendar service = getCalendarService();

            Event event = new Event()
                    .setSummary(idea.getTitle())
                    .setLocation(idea.getPlace() != null ? idea.getPlace() : "")
                    .setDescription(idea.getDescription());

            // Convert LocalDate to Google DateTime (Setting it to 9:00 AM)
            String dateString = idea.getPreciseDate().toString() + "T09:00:00Z";
            DateTime startEndDateTime = new DateTime(dateString);

            EventDateTime eventTime = new EventDateTime().setDateTime(startEndDateTime);
            event.setStart(eventTime);
            event.setEnd(eventTime);

            service.events().insert(calendarId, event).execute();
            System.out.println("Sync Successful: Event added to Google Calendar!");

        } catch (Exception e) {
            System.err.println("Google Calendar Sync Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}