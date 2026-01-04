package com.datlisschen.activityplanner.repository;

import com.datlisschen.activityplanner.model.entity.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ExpeditionRepository extends JpaRepository<Expedition,Long>{

}
