package com.datlisschen.activityplanner.service;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.repository.ExpeditionRepository;
import com.datlisschen.activityplanner.repository.ActivityIdeaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpeditionService {

    private final ExpeditionRepository expeditionRepository;
    private final ActivityIdeaRepository activityIdeaRepository;

    // Single constructor to inject both repositories
    public ExpeditionService(ExpeditionRepository expeditionRepository,
                             ActivityIdeaRepository activityIdeaRepository) {
        this.expeditionRepository = expeditionRepository;
        this.activityIdeaRepository = activityIdeaRepository;
    }

    public List<Expedition> getAllExpeditions() {
        return expeditionRepository.findAll();
    }

    public Expedition getExpeditionById(Long id) {
        return expeditionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expedition not found with id: " + id));
    }

    public void saveExpedition(Expedition expedition) {
        expeditionRepository.save(expedition);
    }

    @Transactional
    public void deleteExpedition(Long id) {
        Expedition expedition = expeditionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expedition not found"));

        List<ActivityIdea> linkedIdeas = activityIdeaRepository.findByExpeditionsId(id);

        for (ActivityIdea idea : linkedIdeas) {
            idea.getExpeditions().remove(expedition);
            activityIdeaRepository.save(idea);
        }

        expeditionRepository.deleteById(id);
    }

    public List<Expedition> searchExpeditions(String query) {
        if (query == null || query.trim().isEmpty()) return getAllExpeditions();
        return expeditionRepository.searchExpeditions(query);
    }
}