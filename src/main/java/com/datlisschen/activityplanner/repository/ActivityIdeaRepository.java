package com.datlisschen.activityplanner.repository;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityIdeaRepository extends JpaRepository<ActivityIdea, Long> {
}