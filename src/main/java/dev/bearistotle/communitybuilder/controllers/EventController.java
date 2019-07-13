package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.data.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//TODO: Add equals and toString methods to all classes
//TODO: Read https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/ and figure
//      out why you should use Set not List for @ManyToMany JPA associations. Refactor accordingly.
@Controller
@RequestMapping(value="events")
public class EventController {

    @Autowired
    private EventDao eventDao;

    @RequestMapping(value="")
    public String index(Model model){

        model.addAttribute("events", eventDao.findAll());
        model.addAttribute("title", "Events");

        return "events/index";
    }

}
