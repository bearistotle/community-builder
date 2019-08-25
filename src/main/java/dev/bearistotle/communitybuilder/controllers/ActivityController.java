package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.ActivityDao;
import dev.bearistotle.communitybuilder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

// TODO: Finish controller for activities pages, incl. getting user from session and adding activities to/getting
//   activities from the logged in user.
// TODO #2: Fix error: (type=Method Not Allowed, status=405) Request method 'GET' not supported

@Controller
@RequestMapping(value = "activities")
public class ActivityController {

    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }
        User user = userDao.findByEmail((String) session.getAttribute("user"));
        List<Activity> activities = user.getActivities();
        model.addAttribute("title","Activities");
        model.addAttribute("activities", activities);
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

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Model model,
                       HttpSession session,
                       @RequestParam Integer activityId){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        Activity activity = activityDao.findOne(activityId);
        model.addAttribute("activity", activity);
        model.addAttribute("title", String.format("Edit %s", activity.getName()));

        return "activities/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(Model model,
                       HttpSession session,
                       @ModelAttribute("activity") @Valid Activity activity,
                       Errors errors){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }
        if (errors.hasErrors()) {
            model.addAttribute("activity", activity);
            model.addAttribute("title", String.format("Edit %s", activity.getName()));

            return "activities/edit";
        }
        if (activityDao.findOne(activity.getActivityId()) == null){

            model.addAttribute("activity", activity);
            model.addAttribute("title", String.format("Edit %s", activity.getName()));

            return "activities/edit";
        }

        Activity storedActivity = activityDao.findOne(activity.getActivityId());
        storedActivity.setName(activity.getName());
        storedActivity.setDescription(activity.getDescription());

        // activityDao.save(storedActivity);

        return "redirect:";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String confirmRemoval(Model model,
                         HttpSession session,
                         @RequestParam Integer activityId){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        return "activities/remove";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(Model model,
                         HttpSession session,
                         @RequestParam Activity activity){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

    return "activities";
    }

}
