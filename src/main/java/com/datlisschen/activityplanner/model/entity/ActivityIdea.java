package com.datlisschen.activityplanner.model.entity;

import com.datlisschen.activityplanner.model.constant.WeatherType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
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

    @Column(length = 10000)
    private String description;

    private String photoPath;
    private LocalDate preciseDate;

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