package com.datlisschen.activityplanner.controller;

import com.datlisschen.activityplanner.model.entity.ActivityIdea;
import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.service.ActivityIdeaService;
import com.datlisschen.activityplanner.service.ExpeditionService;
import com.datlisschen.activityplanner.model.constant.WeatherType;
import com.datlisschen.activityplanner.service.GoogleCalendarService;
import com.datlisschen.activityplanner.service.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
public class ActivityController {

    private final ActivityIdeaService activityService;
    private final ExpeditionService expeditionService;
    private final StorageService storageService;
    // Add the Google Calendar Service
    private final GoogleCalendarService googleCalendarService;

    // Update the constructor to inject the new service
    public ActivityController(ActivityIdeaService activityService,
                              ExpeditionService expeditionService,
                              StorageService storageService,
                              GoogleCalendarService googleCalendarService) {
        this.activityService = activityService;
        this.expeditionService = expeditionService;
        this.storageService = storageService;
        this.googleCalendarService = googleCalendarService;
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


    // Combined the two saveIdea methods into one
    @PostMapping("/idea/save")
    public String saveIdea(@ModelAttribute ActivityIdea idea,
                           @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                           RedirectAttributes redirectAttributes) {
        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = storageService.store(photoFile);
            idea.setPhotoPath(filename);
        }
        // Save to local database first
        activityService.saveIdea(idea);

        // Trigger the Silent Sync to Google Calendar
        // This will only sync if the idea has a date (handled inside the service)
        String eventId = googleCalendarService.addIdeaToCalendar(idea);
        if (eventId != null) {
            idea.setGoogleEventId(eventId);
            redirectAttributes.addFlashAttribute("message", "Idea saved & synced to Google!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Saved locally, but Google sync failed.");
        }

        activityService.saveIdea(idea);
        return "redirect:/";
    }

    //  View Idea Details
    @GetMapping("/idea/edit/{id}")
    public String showIdeaDetails(@PathVariable Long id, Model model) {
        ActivityIdea idea = activityService.getIdeaById(id); // Ensure this method is in your Service
        model.addAttribute("idea", idea);
        model.addAttribute("expeditions", expeditionService.getAllExpeditions());
        return "idea-details";
    }

    // Update/Save Idea
    @PostMapping("/idea/update")
    public String updateIdea(@ModelAttribute ActivityIdea idea,
                             @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {
        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = storageService.store(photoFile);
            idea.setPhotoPath(filename);
        }
        activityService.saveIdea(idea);

        // Also sync on update so changes are reflected in Google
        googleCalendarService.addIdeaToCalendar(idea);

        return "redirect:/";
    }

    // Delete Idea
    @PostMapping("/idea/delete/{id}")
    public String deleteIdea(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ActivityIdea idea = activityService.getIdeaById(id);

        // Delete from Google first using stored ID
        if (idea.getGoogleEventId() != null) {
            googleCalendarService.deleteEvent(idea.getGoogleEventId());
        }
        activityService.deleteIdea(id);
        redirectAttributes.addFlashAttribute("message", "Idea deleted everywhere!");
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