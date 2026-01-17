package com.datlisschen.activityplanner.model.entity;

import com.datlisschen.activityplanner.model.constant.WeatherType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "ideas")
@Data
@NoArgsConstructor
public class ActivityIdea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String place;
    private String googleEventId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    private LocalDateTime lastSyncedAt;

    @Column(length = 10000)
    private String description;

    private String photoPath;

    @ElementCollection(targetClass = WeatherType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "activity_weather", joinColumns = @JoinColumn(name = "activity_id"))
    private Set<WeatherType> recommendedWeather = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "activity_expedition_mapping",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "expedition_id")
    )
    private Set<Expedition> expeditions = new HashSet<>();

    public String getDurationFormatted() {
        if (startTime == null || endTime == null) {
            return "";
        }
        Duration duration = Duration.between(startTime, endTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart(); // Gets the remaining minutes

        if (hours > 0) {
            return hours + "h " + (minutes > 0 ? minutes + "m" : "");
        } else {
            return minutes + "m";
        }
    }
}