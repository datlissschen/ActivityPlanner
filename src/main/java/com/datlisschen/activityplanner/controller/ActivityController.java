package com.datlisschen.activityplanner.controller;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.service.ActivityIdeaService;
import com.datlisschen.activityplanner.service.ExpeditionService;
import com.datlisschen.activityplanner.model.constant.WeatherType;
import com.datlisschen.activityplanner.service.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
public class ActivityController {
    // 1. Declare all services as private final fields
    private final ActivityIdeaService activityService;
    private final ExpeditionService expeditionService;
    private final StorageService storageService; // Added this

    // 2. Inject all three services into the constructor
    public ActivityController(ActivityIdeaService activityService,
                              ExpeditionService expeditionService,
                              StorageService storageService) {
        this.activityService = activityService;
        this.expeditionService = expeditionService;
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String showDashboard(Model model) {
        LocalDate today = LocalDate.now();
        List<Expedition> expeditions = expeditionService.getAllExpeditions();

        // 1. Find the current expedition (where today is between start and end)
        Expedition currentExpedition = expeditions.stream()
                .filter(e -> (today.isEqual(e.getStartDate()) || today.isAfter(e.getStartDate())) &&
                        (today.isEqual(e.getEndDate()) || today.isBefore(e.getEndDate())))
                .findFirst()
                .orElse(null);

        // 2. Fetch ideas only for that expedition
        List<ActivityIdea> filteredIdeas;
        if (currentExpedition != null) {
            filteredIdeas = activityService.getIdeasByExpedition(currentExpedition.getId());
        } else {
            filteredIdeas = Collections.emptyList();
        }

        model.addAttribute("expeditions", expeditions);
        model.addAttribute("currentExpedition", currentExpedition);
        model.addAttribute("ideas", filteredIdeas);

        return "index";
    }

    @GetMapping("/expedition/new")
    public String showCreateExpeditionForm(Model model) {
        model.addAttribute("expedition", new Expedition());
        model.addAttribute("weatherTypes", WeatherType.values());
        return "create-expedition";
    }

    @PostMapping("/expedition/save")
    public String saveExpedition(@ModelAttribute Expedition expedition) {
        expeditionService.saveExpedition(expedition);
        return "redirect:/";
    }

    @GetMapping("/expedition/overview/{id}")
    public String showExpeditionOverview(@PathVariable Long id, Model model) {
        Expedition expedition = expeditionService.getExpeditionById(id);
        model.addAttribute("expedition", expedition);
        model.addAttribute("weatherTypes", WeatherType.values());
        return "expedition-details";
    }

    @PostMapping("/expedition/delete/{id}")
    public String deleteExpedition(@PathVariable Long id) {
        expeditionService.deleteExpedition(id);
        return "redirect:/";
    }

    @GetMapping("/idea/new")
    public String showCreateIdeaForm(Model model) {
        model.addAttribute("idea", new ActivityIdea());
        List<Expedition> expeditions = expeditionService.getAllExpeditions();

        model.addAttribute("expeditions", expeditionService.getAllExpeditions());
        return "create-idea";
    }


    // 3. Combined the two saveIdea methods into one
    @PostMapping("/idea/save")
    public String saveIdea(@ModelAttribute ActivityIdea idea,
                           @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {

        // Use 'storageService' (lowercase s) which is now a field
        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = storageService.store(photoFile);
            idea.setPhotoPath(filename);
        }

        // Use 'activityService' to match the field name defined above
        activityService.saveIdea(idea);
        return "redirect:/";
    }



}