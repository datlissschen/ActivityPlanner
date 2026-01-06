package com.datlisschen.activityplanner.service;

import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.repository.ExpeditionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpeditionService {

    private final ExpeditionRepository expeditionRepository;
    public ExpeditionService(ExpeditionRepository expeditionRepository) {
        this.expeditionRepository = expeditionRepository;
    }

    public List<Expedition> getAllExpeditions() {
        return expeditionRepository.findAll();
    }

    public Expedition saveExpedition(Expedition expedition) {
        return expeditionRepository.save(expedition); // add logic that the end date can not be before the start date
    }
}
