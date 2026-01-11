package com.datlisschen.activityplanner.service;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.repository.ActivityIdeaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ActivityIdeaService {

    private final ActivityIdeaRepository activityIdeaRepository;

    public List<ActivityIdea> getIdeasByExpedition(Long expeditionId) {
        return activityIdeaRepository.findByExpeditionsId(expeditionId);
    }

    public ActivityIdeaService(ActivityIdeaRepository activityIdeaRepository) {
        this.activityIdeaRepository = activityIdeaRepository;
    }

    @Transactional
    public ActivityIdea createActivityWithStations(ActivityIdea idea, Set<Expedition> stations) {
        idea.getExpeditions().addAll(stations);
        return activityIdeaRepository.save(idea);
    }

    public void saveIdea(ActivityIdea idea) {
        activityIdeaRepository.save(idea);
    }

    public List<ActivityIdea> getAllIdeas() {
        return activityIdeaRepository.findAll();
    }

    public ActivityIdea getIdeaById(Long id) {
        return activityIdeaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Idea not found"));
    }

    public void deleteIdea(Long id) {
        activityIdeaRepository.deleteById(id);
    }
}