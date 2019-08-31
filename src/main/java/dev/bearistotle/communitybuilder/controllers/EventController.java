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

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Model model,
                       HttpSession session,
                       @RequestParam("eventId") int eventId){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        Event event = eventDao.findOne(eventId);
        List<Location> locations = (List<Location>) locationDao.findAll();
        AddEventForm form = new AddEventForm(event, locations);
        model.addAttribute("form", form);
        model.addAttribute("eventId", eventId);
        model.addAttribute("title", "Edit Event: " + event.getName());

        return "events/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(Model model,
                       HttpSession session,
                       @Valid @ModelAttribute("form") AddEventForm form,
                       Errors errors,
                       @RequestParam("eventId") int eventId){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        Event event = eventDao.findOne(eventId);
        ArrayList<Location> locationList = (ArrayList<Location>) form.getLocations();

        if (errors.hasErrors()) {

            model.addAttribute("title", "Edit Event: " + event.getName());
            model.addAttribute("form", form);

            return "events/edit";
        }

        event.setName(form.getName());
        event.setDescription(form.getDescription());
        event.setDate(LocalDate.parse(form.getDate()));
        event.setRecurrencePattern(form.getRecurrencePattern());
        event.setStartTime(LocalTime.parse(form.getStartTime()));
        event.setEndTime(LocalTime.parse(form.getEndTime()));
        event.setLocation(locationList.get(0));
        event.setActivities(form.getActivities());
        event.setMinParticipants(form.getMinParticipants());
        event.setMaxParticipants(form.getMaxParticipants());

        eventDao.save(event);

        return "redirect:";
    }

}
