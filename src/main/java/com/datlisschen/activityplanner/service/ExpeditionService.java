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

    public void saveExpedition(Expedition expedition) {
        expeditionRepository.save(expedition);
    }
}
