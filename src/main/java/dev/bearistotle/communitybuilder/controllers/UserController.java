package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.User;
import dev.bearistotle.communitybuilder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
//TODO; create ability to save user
@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public String index(Model model){

        model.addAttribute("users", userDao.findAll());
        model.addAttribute("title", "Users");

        return "user/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model){
        User user = new User();
        model.addAttribute("title", "Add User");
        model.addAttribute("user", user);

        return "user/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Model model,
                      @ModelAttribute @Valid User newUser,
                      Errors errors, @RequestParam String verify){
        if (errors.hasErrors()){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            return "user/add";
        }
        if (verify != newUser.getPwHash()){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            model.addAttribute("verifyError", "Password and verification must match.");
            return "user/add";

        }
        userDao.save(newUser);
        return "redirect:";
    }

}