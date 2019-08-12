package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.ActivityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

// TODO: Finish controller for activities pages, incl. getting user from session and adding activities to/getting
//  activities from the logged in user.
@Controller
@RequestMapping(value = "activities")
public class ActivityController {

    @Autowired
    ActivityDao activityDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }
        List<Activity> activities = (List<Activity>) activityDao.findAll();
        User user = (User) session.getAttribute("user");
        model.addAttribute("title","Activities");
        model.addAttribute("activities", activities);
        model.addAttribute("user", user);

        return "activities/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }
        Activity activity = new Activity();
        // TODO: create add activity form in models and pass it into the view here.
        return "activities/add";
    }
}
