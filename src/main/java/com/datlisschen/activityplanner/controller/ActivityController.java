package com.datlisschen.activityplanner.controller;

import com.datlisschen.activityplanner.service.ActivityIdeaService;
import com.datlisschen.activityplanner.service.ExpeditionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
