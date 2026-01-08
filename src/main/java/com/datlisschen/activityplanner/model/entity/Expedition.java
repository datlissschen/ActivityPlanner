package com.datlisschen.activityplanner.model.entity;

import com.datlisschen.activityplanner.model.constant.WeatherType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "expeditions")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Expedition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String expeditionDescription;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    @ElementCollection(targetClass = WeatherType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "expedition_weather", joinColumns = @JoinColumn(name = "expedition_id"))
    private Set<WeatherType> typicalWeather;
}
