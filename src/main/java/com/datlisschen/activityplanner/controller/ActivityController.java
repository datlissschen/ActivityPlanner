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
    private final StorageService storageService;

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

        // Find the current active trip
        Expedition currentExpedition = expeditions.stream()
                .filter(e -> (today.isEqual(e.getStartDate()) || today.isAfter(e.getStartDate())) &&
                        (today.isEqual(e.getEndDate()) || today.isBefore(e.getEndDate())))
                .findFirst()
                .orElse(null);

        // Get ideas only for the active trip
        List<ActivityIdea> filteredIdeas = (currentExpedition != null)
                ? activityService.getIdeasByExpedition(currentExpedition.getId())
                : Collections.emptyList();

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
        return "redirect:/expedition/list";
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

        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = storageService.store(photoFile);
            idea.setPhotoPath(filename);
        }

        activityService.saveIdea(idea);
        return "redirect:/";
    }

    // 1. View Idea Details
    @GetMapping("/idea/edit/{id}")
    public String showIdeaDetails(@PathVariable Long id, Model model) {
        ActivityIdea idea = activityService.getIdeaById(id); // Ensure this method is in your Service
        model.addAttribute("idea", idea);
        model.addAttribute("expeditions", expeditionService.getAllExpeditions());
        return "idea-details";
    }

    // 2. Update/Save Idea
    @PostMapping("/idea/update")
    public String updateIdea(@ModelAttribute ActivityIdea idea,
                             @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {
        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = storageService.store(photoFile);
            idea.setPhotoPath(filename);
        }
        activityService.saveIdea(idea);
        return "redirect:/";
    }

    // 3. Delete Idea
    @PostMapping("/idea/delete/{id}")
    public String deleteIdea(@PathVariable Long id) {
        activityService.deleteIdea(id); // Ensure this method is in your Service
        return "redirect:/";
    }

    @GetMapping("/expedition/list")
    public String listAllExpeditions(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Expedition> expeditions = expeditionService.searchExpeditions(search);
        model.addAttribute("expeditions", expeditions);
        model.addAttribute("searchQuery", search);
        return "expedition-list";
    }

    @GetMapping("/idea/list")
    public String listAllIdeas(@RequestParam(value = "search", required = false) String search, Model model) {
        List<ActivityIdea> ideas = activityService.searchIdeas(search);
        model.addAttribute("ideas", ideas);
        model.addAttribute("searchQuery", search);
        return "idea-list";
    }
}