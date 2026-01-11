package com.datlisschen.activityplanner.repository;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityIdeaRepository extends JpaRepository<ActivityIdea, Long> {
    List<ActivityIdea> findByExpeditionsId(Long expeditionId);

    @Query("SELECT i FROM ActivityIdea i WHERE " +
            "LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.place) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ActivityIdea> searchIdeas(@Param("query") String query);
}
