package com.datlisschen.activityplanner.config;

import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.repository.ExpeditionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ExpeditionRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Expedition defaultExp = new Expedition();
                defaultExp.setName("First Journey");
                defaultExp.setStartDate(LocalDate.now());
                defaultExp.setEndDate(LocalDate.now().plusDays(7));
                repository.save(defaultExp);
                System.out.println("DEBUG: Created default expedition.");
            }
        };
    }
}