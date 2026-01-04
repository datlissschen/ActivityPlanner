package com.datlisschen.activityplanner.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

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
    private String expeditionName;

    @Column(nullable = false)
    private String expeditionDescription;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    private String typicalWeather;
}
