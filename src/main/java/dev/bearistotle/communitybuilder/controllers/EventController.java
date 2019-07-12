package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.data.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


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
