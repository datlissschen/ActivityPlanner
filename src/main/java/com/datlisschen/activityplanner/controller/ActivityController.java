package com.datlisschen.activityplanner.controller;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.service.ActivityIdeaService;
import com.datlisschen.activityplanner.service.ExpeditionService;
import com.datlisschen.activityplanner.model.constant.WeatherType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

//takes the data from the services and injects them into the Thymelaf HTML
@Controller
public class ActivityController {
    private final ActivityIdeaService activityService;
    private final ExpeditionService expeditionService;

    public ActivityController(ActivityIdeaService activityService, ExpeditionService expeditionService)
    {
        this.activityService = activityService;
        this.expeditionService = expeditionService;
    }

    @GetMapping("/expedition/new")
    public String showCreateExpeditionForm(Model model) {
        model.addAttribute("expedition", new Expedition());
        model.addAttribute("weatherTypes", WeatherType.values()); // For the multiple choice weather
        return "create-expedition";
    }

    @GetMapping("/")
    public String showDashboard(Model model) {
        List<Expedition> expeditions = expeditionService.getAllExpeditions();
        // Use an empty list instead of null to prevent Thymeleaf from panicking
        model.addAttribute("expeditions", expeditions != null ? expeditions : Collections.emptyList());
        return "index";
    }

    @PostMapping("/expedition/save")
    public String saveExpedition(@ModelAttribute Expedition expedition) {
        expeditionService.saveExpedition(expedition);
        return "redirect:/"; // Go back to the main scrapbook page
    }

    @GetMapping("/expedition/overview/{id}")
    public String showExpeditionOverview(@PathVariable Long id, Model model) {
        // Find the specific expedition by ID
        Expedition expedition = expeditionService.getExpeditionById(id);
        model.addAttribute("expedition", expedition);
        model.addAttribute("weatherTypes", WeatherType.values());
        return "expedition-details"; // We will create this new template
    }

    @PostMapping("/expedition/delete/{id}")
    public String deleteExpedition(@PathVariable Long id) {
        expeditionService.deleteExpedition(id);
        return "redirect:/"; // Go back to empty dashboard
    }

    @GetMapping("/idea/new")
    public String showCreateIdeaForm(Model model) {
        model.addAttribute("idea", new ActivityIdea());
        System.out.println("DEBUG: Entering showCreateIdeaForm");
        model.addAttribute("expeditions", expeditionService.getAllExpeditions());
        return "create-idea";
    }

    @PostMapping("/idea/save")
    public String saveIdea(@ModelAttribute ActivityIdea idea) {
        activityService.saveIdea(idea); // Ensure this service method exists
        return "redirect:/";
    }

    @PostMapping("/idea/save")
    public String saveIdea(@ModelAttribute ActivityIdea idea,
                           @RequestParam("photoFile") MultipartFile photoFile) {

        String filename = storageService.store(photoFile);

        if (filename != null) {
            idea.setPhotoPath(filename);
        }

        activityIdeaService.saveIdea(idea);
        return "redirect:/";
    }
}


