package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value="")
    public String index(Model model){
        User user = new User();
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("title", "Users");
        model.addAttribute("user", user);

        return "user/index";
    }

    @RequestMapping(value="/add")
    public String add(Model model){

        return "user/add";
    }
}