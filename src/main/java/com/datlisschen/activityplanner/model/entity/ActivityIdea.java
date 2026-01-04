package com.datlisschen.activityplanner.model.entity;

import com.datlisschen.activityplanner.model.constant.WheatherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    @ElementCollection(targetClass = WheatherType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "activity_weather", joinColumns = @JoinColumn(name = "activity_id"))
    private Set<WheatherType> recommendedWeather;

    @ManyToMany
    @JoinTable(
            name = "activity_expedition_mapping",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "expedition_id")
    )
    private Set<Expedition> expeditions = new HashSet<>();
}
