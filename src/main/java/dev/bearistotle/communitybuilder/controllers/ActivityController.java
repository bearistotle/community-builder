package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.ActivityDao;
import dev.bearistotle.communitybuilder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

// TODO: Finish controller for activities pages, incl. getting user from session and adding activities to/getting
//   activities from the logged in user.

// TODO #1: Currently, when an activity is added, a new user is created with the logged in user's username and null in
//   everything else. Fix this.

@Controller
@RequestMapping(value = "activities")
public class ActivityController {

    @Autowired
    ActivityDao activityDao;
    @Autowired
    UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }
        List<Activity> activities = (List<Activity>) activityDao.findAll();
        User user = userDao.findByEmail((String) session.getAttribute("user"));
        model.addAttribute("title","Activities");
        model.addAttribute(activities);
        model.addAttribute(user);

        return "activities/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }
        Activity activity = new Activity();
        User user = userDao.findByEmail((String) session.getAttribute("user"));
        model.addAttribute(activity);
        model.addAttribute(user);
        model.addAttribute("title","Add Activity");

        return "activities/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Model model,
                      HttpSession session,
                      @Valid @ModelAttribute("newActivity") Activity newActivity,
                      Errors errors){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        // validate form
        if (errors.hasErrors()){
            model.addAttribute("title", "Activities");
            model.addAttribute("activity", newActivity);
            return "activities/add";
        }

        // save activity info
        newActivity.addUser(user);
        activityDao.save(newActivity);
        user.addActivity(newActivity);

        return "redirect:";
    }
}
