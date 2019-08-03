package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.HashUtils;
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

import java.lang.Character;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.validation.Valid;

// TODO: Solidify understanding of exception handling and ensure code properly handles all necesssary exceptions.

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
                      @ModelAttribute("newUser") User newUser,
                      Errors errors,
                      @RequestParam String password,
                      @RequestParam String verify) throws Exception {
        // validation
        if (errors.hasErrors()){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            return "user/add";
        }
        if (password.isEmpty()) {
            model.addAttribute("title", "Add User");
            model.addAttribute("passwordError","A password is required.");
            return "user/add";
        }
        if (password.length() < 8 || password.length() > 25) {
            model.addAttribute("title", "Add User");
            model.addAttribute("passwordError", "Password must be 8--25 characters.");
            return "user/add";
        }

        boolean lower = false;
        boolean upper = false;
        boolean digit = false;
        boolean special = false;
        boolean whitespace = false;
        for (Character c: password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                lower = true;
            }
            if (Character.isUpperCase(c)) {
                upper = true;
            }
            if (Character.isDigit(c)) {
                digit = true;
            }
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                special = true;
            }
            if (Character.isWhitespace(c)) {
                whitespace = true;
            }
        }
        if (whitespace){
            model.addAttribute("title", "Add User");
            model.addAttribute("passwordError", "Password cannot contain spaces or tabs.");
            return "user/add";
        }
        if (!(lower && upper && digit && special)){
            model.addAttribute("title", "Add User");
            model.addAttribute("passwordError", "Password must contain at least one character" +
                    "from each of a--z, A--Z, 0--9, and the special characters (e.g., !@#$%^&*:;).");
            return "user/add";
        }
        if (!verify.equals(password)){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            model.addAttribute("verifyError", "Password and verification must match.");
            return "user/add";
        }

        List<User> users = (List<User>) userDao.findAll();
        boolean existsByUsername = false;
        boolean existsByEmail = false;

        for (User user : users){
            if (user.getUsername().equals(newUser.getUsername())){
                existsByUsername = true;
            }
            if (user.getEmail().equals(newUser.getEmail())) {
                existsByEmail = true;
            }
        }
        if (existsByUsername == true){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            model.addAttribute("usernameError","That username is taken. Please choose another.");
            return "user/add";
        }
        if (existsByEmail == true){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            model.addAttribute("emailError","That email is taken. Please choose another.");
            return "user/add";
        }


        String pwHash = HashUtils.getSaltedHash(password);
        newUser.setPwHash(pwHash);
        userDao.save(newUser);

        return "redirect:";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){
        // check if already logged in
        // display "You are already logged in as user X... Do you want to log out?" with logout button
        // render login form
        User user = new User();
        model.addAttribute("title","Log In");
        model.addAttribute("user", user);

        return "user/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model,
                        @ModelAttribute("user") @Valid User user,
                        @RequestParam String password) throws Exception {
        if (userDao.findByUsername(user.getUsername()) != null) {
            User registeredUser = userDao.findByUsername(user.getUsername());
            if (HashUtils.checkPassword(password, registeredUser.getPwHash())) {
                //create session and redirect
                return "user/index";
            }
        }
        // if login fails, return to login screen with error message
        model.addAttribute("title", "Log In");
        model.addAttribute("user", user);
        model.addAttribute("loginError","Username or password did not match any registered user." +
                " Please check them and try again.");

        return "user/login";
    }
}