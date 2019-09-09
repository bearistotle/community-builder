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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model,
                      HttpSession session,
                      @Valid @ModelAttribute("form") AddAvailabilityForm form,
                      Errors errors){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        if (errors.hasErrors()){

            List<Activity> activities = (List<Activity>) activityDao.findAll();
            List<Location> locations = (List<Location>) locationDao.findAll();

            form.setActivities(activities);
            form.setLocations(locations);
            model.addAttribute("title", "Add Availability");
            model.addAttribute("form", form);

            return "availabilities/add";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        ArrayList<Activity> activities = (ArrayList<Activity>) form.getActivities();
        LocalDate date = LocalDate.parse(form.getDate());
        LocalTime startTime = LocalTime.parse(form.getStartTime());
        LocalTime endTime = LocalTime.parse(form.getEndTime());
        String recurrencePattern = form.getRecurrencePattern();
        Location location = form.getLocations().get(0);
        Availability newAvailability = new Availability(activities,
                                                        user,
                                                        date,
                                                        startTime,
                                                        endTime,
                                                        recurrencePattern,
                                                        location);
        availabilityDao.save(newAvailability);
        user.addAvailability(newAvailability);
        for (Activity activity: newAvailability.getActivities()){
            activity.addAvailability(newAvailability);
        }
        return "redirect:";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(Model model,
                       HttpSession session,
                       @RequestParam("availabilityId") int availabilityId){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        Availability availability = availabilityDao.findOne(availabilityId);
        List<Activity> activities = (List<Activity>) activityDao.findAll();
        List<Location> locations = (List<Location>) locationDao.findAll();
        AddAvailabilityForm form = new AddAvailabilityForm(availability, activities, locations);
        model.addAttribute("form", form);
        model.addAttribute("availabilityId", availabilityId);
        model.addAttribute("title", "Edit Availability");

        return "availabilities/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(Model model,
                       HttpSession session,
                       @Valid AddAvailabilityForm form,
                       Errors errors,
                       @RequestParam("availabilityId") int availabilityId){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        if (errors.hasErrors()){
            model.addAttribute("title", "Edit Availability");
            model.addAttribute("form", form);

            return "availabilities/edit";
        }

        Availability storedAvailability = availabilityDao.findOne(availabilityId);
        storedAvailability.setDate(LocalDate.parse(form.getDate()));
        storedAvailability.setRecurrencePattern(form.getRecurrencePattern());
        storedAvailability.setStartTime(LocalTime.parse(form.getStartTime()));
        storedAvailability.setEndTime(LocalTime.parse(form.getEndTime()));
        storedAvailability.setActivities(form.getActivities());
        List<Location> locations = form.getLocations();
        Location location = locations.get(0);
        storedAvailability.setLocation(location);

        availabilityDao.save(storedAvailability);

        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String confirmRemoval(Model model,
                                 HttpSession session,
                                 @RequestParam("availabilityId") int availabilityId){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        Availability availability = availabilityDao.findOne(availabilityId);
        model.addAttribute("availability", availability);

        return "availabilities/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String remove(Model model,
                         HttpSession session,
                         @RequestParam("availabilityId") int availabilityId){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        Availability availability = availabilityDao.findOne(availabilityId);
        user.removeAvailability(availability);
        for (Activity activity: availability.getActivities()){
            activity.removeAvailability(availability);
        }
        availabilityDao.delete(availabilityId);

        return "redirect:";
    }
}
