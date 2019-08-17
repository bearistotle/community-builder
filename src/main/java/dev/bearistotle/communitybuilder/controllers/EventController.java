package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.Event;
import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.EventDao;
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

    @RequestMapping(value="")
    public String index(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }
        // TODO: refactor to get events for the specific user
        User user = userDao.findByEmail((String) session.getAttribute("user"));
        model.addAttribute("events", eventDao.findAll());
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
                      Errors errors){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        User user = userDao.findByEmail((String) session.getAttribute("user"));
        // validate form
        if (errors.hasErrors()){
            model.addAttribute("title", "Events");
            model.addAttribute("event", newEvent);
            return "events/add";
        }

        // save activity info
        newEvent.addUser(user);
        eventDao.save(newEvent);
        user.addEvent(newEvent);

        return "redirect:";
    }
}
