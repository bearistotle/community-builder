package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.*;
import dev.bearistotle.communitybuilder.models.data.EventDao;
import dev.bearistotle.communitybuilder.models.data.LocationDao;
import dev.bearistotle.communitybuilder.models.data.UserDao;
import dev.bearistotle.communitybuilder.models.forms.AddEventForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//TODO: Read https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/ and figure
//      out why you should use Set not List for @ManyToMany JPA associations. Refactor accordingly.
@Controller
@RequestMapping(value="events")
public class EventController {

    @Autowired
    private EventDao eventDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LocationDao locationDao;

    /** TODO: Figure out how to use @InitBinder to fix typeMismatch error
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, "date", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    */

    @RequestMapping(value="")
    public String index(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        model.addAttribute("events", user.getEvents());
        model.addAttribute("title", "Events");
        model.addAttribute("user", user);

        return "events/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        List<Location> locations = (List<Location>) locationDao.findAll();
        List<Activity> activities = user.getActivities();
        AddEventForm form = new AddEventForm(locations, activities);
        model.addAttribute("form", form);
        model.addAttribute("title","Add Event");

        return "events/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Model model,
                      HttpSession session,
                      @Valid @ModelAttribute("form") AddEventForm form,
                      Errors errors){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));

        if (errors.hasErrors()){
            model.addAttribute("title", "Events");
            model.addAttribute("form", form);
            // TODO: Remove this bit of code below once done debugging.
            List<ObjectError> errorsToPrint= errors.getAllErrors();
            for (ObjectError e: errorsToPrint){
                System.out.println(e);
            }
            return "events/add";
        }

        LocalDate date = LocalDate.parse(form.getDate());
        LocalTime startTime = LocalTime.parse(form.getStartTime());
        LocalTime endTime = LocalTime.parse(form.getEndTime());
        ArrayList<Location> locationList = (ArrayList<Location>) form.getLocations();
        Location location = locationList.get(0);
        ArrayList<Activity> activities = (ArrayList<Activity>) form.getActivities();

        Event newEvent = new Event(form.getName(),
                form.getDescription(),
                date,
                form.getRecurrencePattern(),
                startTime,
                endTime,
                location,
                activities,
                form.getMinParticipants(),
                form.getMaxParticipants());

        newEvent.addUser(user);
        eventDao.save(newEvent);
        user.addEvent(newEvent);

        return "redirect:";
    }
}
