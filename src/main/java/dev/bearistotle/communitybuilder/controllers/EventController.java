package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.Event;
import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.EventDao;
import dev.bearistotle.communitybuilder.models.data.UserDao;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//TODO: Add equals and toString methods to all classes
//TODO: Read https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/ and figure
//      out why you should use Set not List for @ManyToMany JPA associations. Refactor accordingly.
@Controller
@RequestMapping(value="events")
public class EventController {

    @Autowired
    private EventDao eventDao;
    @Autowired
    private UserDao userDao;

    // TODO: Figure out how to use @InitBinder to fix typeMismatch error
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, "date", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }


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

        Event event = new Event();
        User user = userDao.findByEmail((String) session.getAttribute("user"));
        model.addAttribute(event);
        model.addAttribute(user);
        model.addAttribute("title","Add Event");

        return "events/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Model model,
                      HttpSession session,
                      @Valid @ModelAttribute("newEvent") Event newEvent,
                      Errors errors,
                      @RequestParam Integer minParticipants,
                      @RequestParam Integer maxParticipants){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));

        if (errors.hasErrors()){
            model.addAttribute("title", "Events");
            model.addAttribute("event", newEvent);
            List<ObjectError> errorsToPrint= errors.getAllErrors();
            for (ObjectError e: errorsToPrint){
                System.out.println(e);
            }
            return "events/add";
        }
        HashMap<String, Integer> numParticipants = new HashMap<>();
        numParticipants.put("min", minParticipants);
        numParticipants.put("max", maxParticipants);
        newEvent.addUser(user);
        newEvent.setNumParticipants(numParticipants);
        eventDao.save(newEvent);
        user.addEvent(newEvent);

        return "redirect:";
    }
}
