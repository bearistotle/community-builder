package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.*;
import dev.bearistotle.communitybuilder.models.data.*;
import dev.bearistotle.communitybuilder.models.forms.AddEventForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//TODO: Read https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/ and figure
//      out why you should use Set not List for @ManyToMany JPA associations. Refactor accordingly.

// TODO: Figure out why activities won't get saved to events. It has something to do with the fact that the many to many
//    connection is set up through the availability object. Tried to save events with availability dao instead of event,
//    but no change. Consider creating parent object that both availability and event inherit from, then setting up the
//    many to many relations etc on the availability and event objects. Or on the parent?
@Controller
@RequestMapping(value="events")
public class EventController {

    @Autowired
    private EventDao eventDao;
    @Autowired
    private AvailabilityDao availabilityDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private ActivityDao activityDao;

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
                user,
                activities,
                date,
                startTime,
                endTime,
                form.getRecurrencePattern(),
                location,
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
        List<Activity> activities = (List<Activity>) activityDao.findAll();
        AddEventForm form = new AddEventForm(event, locations, activities);
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

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String confirmRemoval(Model model,
                         HttpSession session,
                         @RequestParam("eventId") int eventId){

        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        Event event = eventDao.findOne(eventId);
        model.addAttribute("title", "Remove Event");
        model.addAttribute("eventName", event.getName());
        model.addAttribute("eventId", eventId);
        return "events/remove";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(Model model,
                         HttpSession session,
                         @RequestParam("eventId") int eventId){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        Event event = eventDao.findOne(eventId);
        user.removeEvent(event);
        eventDao.delete(eventId);

        return "redirect:";
    }

}
