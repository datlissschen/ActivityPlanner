package com.datlisschen.activityplanner.repository;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityIdeaRepository extends JpaRepository<ActivityIdea, Long> {
    List<ActivityIdea> findByExpeditionsId(Long expeditionId);
}
