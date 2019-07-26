package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.data.ActivityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

// TODO: Finish controller for activities pages
@Controller
@RequestMapping(value = "activities")
public class ActivityController {

    @Autowired
    ActivityDao activityDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model){
        List<Activity> activities = (List<Activity>) activityDao.findAll();
        model.addAttribute("title","Activities");
        model.addAttribute("activities", activities);

        return "activities/index";
    }
}
