package com.datlisschen.activityplanner.controller;

import com.datlisschen.activityplanner.model.entity.Expedition;
import com.datlisschen.activityplanner.service.ActivityIdeaService;
import com.datlisschen.activityplanner.service.ExpeditionService;
import com.datlisschen.activityplanner.model.constant.WeatherType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/")
    public String showDashboard(Model model)
    {
        model.addAttribute("expeditions", expeditionService.getAllExpeditions());
        return "index";
    }

    @GetMapping("/expedition/new")
    public String showCreateExpeditionForm(Model model) {
        model.addAttribute("expedition", new Expedition());
        model.addAttribute("weatherTypes", WeatherType.values()); // For the multiple choice weather
        return "create-expedition";
    }

    @PostMapping("/expedition/save")
    public String saveExpedition(@ModelAttribute Expedition expedition) {
        expeditionService.saveExpedition(expedition);
        return "redirect:/"; // Go back to the main scrapbook page
    }
}


