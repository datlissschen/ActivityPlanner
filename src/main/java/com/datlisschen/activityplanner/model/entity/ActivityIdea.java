package com.datlisschen.activityplanner.model.entity;

import com.datlisschen.activityplanner.model.constant.WeatherType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate preciseDate;
    private String title;
    private String place;

    private String googleEventId;

    public String getGoogleEventId() { return googleEventId; }
    public void setGoogleEventId(String googleEventId) { this.googleEventId = googleEventId; }

    private LocalDateTime lastSyncedAt;

    public LocalDateTime getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(LocalDateTime lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    @Column(length = 10000)
    private String description;

    private String photoPath;

    // Initializing the Set prevents NullPointerExceptions in your Service
    @ElementCollection(targetClass = WeatherType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "activity_weather", joinColumns = @JoinColumn(name = "activity_id"))
    private Set<WeatherType> recommendedWeather = new HashSet<>();

    // Change: Your Service uses .getExpeditions(), so keep this name
    @ManyToMany
    @JoinTable(
            name = "activity_expedition_mapping",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "expedition_id")
    )
    private Set<Expedition> expeditions = new HashSet<>();

    /* --- Add these inside the ActivityIdea class at the bottom --- */

    public Set<Expedition> getExpeditions() {
        if (this.expeditions == null) {
            this.expeditions = new HashSet<>();
        }
        return expeditions;
    }

    public void setExpeditions(Set<Expedition> expeditions) {
        this.expeditions = expeditions;
    }
}