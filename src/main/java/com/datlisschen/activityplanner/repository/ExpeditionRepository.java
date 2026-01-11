package com.datlisschen.activityplanner.repository;

import com.datlisschen.activityplanner.model.entity.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpeditionRepository extends JpaRepository<Expedition, Long> {
    @Query("SELECT e FROM Expedition e WHERE " +
            "LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.expeditionDescription) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Expedition> searchExpeditions(@Param("query") String query);
}
