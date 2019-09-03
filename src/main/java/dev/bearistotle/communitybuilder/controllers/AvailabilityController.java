package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.Availability;
import dev.bearistotle.communitybuilder.models.Location;
import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.ActivityDao;
import dev.bearistotle.communitybuilder.models.data.AvailabilityDao;
import dev.bearistotle.communitybuilder.models.data.LocationDao;
import dev.bearistotle.communitybuilder.models.data.UserDao;
import dev.bearistotle.communitybuilder.models.forms.AddAvailabilityForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "availabilities")
public class AvailabilityController {

    @Autowired
    AvailabilityDao availabilityDao;
    @Autowired
    UserDao userDao;
    @Autowired
    LocationDao locationDao;
    @Autowired
    ActivityDao activityDao;

    @RequestMapping(value = "")
    public String index(Model model, HttpSession session){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        model.addAttribute("availabilities", user.getAvailabilities());
        model.addAttribute("title", "Availabilities: " + user.getUsername());

        return "availabilities/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model, HttpSession session){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        List<Activity> activities = (List<Activity>) activityDao.findAll();
        List<Location> locations = (List<Location>) locationDao.findAll();
        AddAvailabilityForm form = new AddAvailabilityForm(activities, locations);

        model.addAttribute("form", form);
        model.addAttribute("title", "Add Availability");

        return "availabilities/add";
    }

}
