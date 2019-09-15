package dev.bearistotle.communitybuilder.controllers;

import dev.bearistotle.communitybuilder.models.Availability;
import dev.bearistotle.communitybuilder.models.CalendarItem;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

// TODO #2: Refactor to account for making User parent class for Resident and PropertyManager
// TODO: Solidify understanding of exception handling and ensure code properly handles all necessary exceptions.
@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public String index(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }

        // TODO: 1) Get calendarItems with dates falling in the current week; 2) Get calendarItems that recur weekly;
        //   3) Get calendarItems that recur monthly; 4) Get calendarItems that recur yearly.
        User user = userDao.findByEmail((String) session.getAttribute("user"));
        ArrayList<LocalDate> datesThisWeek = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate monday = today;

        while (monday.getDayOfWeek() != DayOfWeek.MONDAY){
            monday = monday.minusDays(1);
        }

        for (int i = 0; i < 7; i++){
            LocalDate date = monday.plusDays(i);
            datesThisWeek.add(date);
        }

        // Create list of all Availabilities and Events
        ArrayList<CalendarItem> calendarItems = new ArrayList<>();
        calendarItems.addAll(user.getAvailabilities());
        calendarItems.addAll(user.getEvents());

        // List of HashMaps with hour as key and list of Availabilities during that hour as value for each day
        HashMap<Integer,ArrayList<CalendarItem>> monItems = new HashMap<>();
        HashMap<Integer,ArrayList<CalendarItem>> tuesItems = new HashMap<>();
        HashMap<Integer,ArrayList<CalendarItem>> wedItems = new HashMap<>();
        HashMap<Integer,ArrayList<CalendarItem>> thursItems = new HashMap<>();
        HashMap<Integer,ArrayList<CalendarItem>> friItems = new HashMap<>();
        HashMap<Integer,ArrayList<CalendarItem>> satItems = new HashMap<>();
        HashMap<Integer,ArrayList<CalendarItem>> sunItems = new HashMap<>();


        ArrayList<HashMap<Integer,ArrayList<CalendarItem>>> dayMaps = new ArrayList<>();
        dayMaps.add(monItems);
        dayMaps.add(tuesItems);
        dayMaps.add(wedItems);
        dayMaps.add(thursItems);
        dayMaps.add(friItems);
        dayMaps.add(satItems);
        dayMaps.add(sunItems);

        for (HashMap<Integer,ArrayList<CalendarItem>> dayMap: dayMaps) {

            // Create a map entry for each hour of the day
            for (int i = 1; i < 25; i++) {
                ArrayList<CalendarItem> emptyList = new ArrayList<>();
                dayMap.put(i, emptyList);

            }
        }


        for (int i = 1; i < 8; i++) {
            HashMap<Integer,ArrayList<CalendarItem>> dayMap = dayMaps.get(i-1);
            LocalDate date = datesThisWeek.get(i-1);

            for (CalendarItem calendarItem : calendarItems) {

                if (calendarItem.getDate().equals(date)){
                    // Add Availability to ArrayList that corresponds to the hour of its startTime in the HashMap for
                    // the day.
                    ArrayList<CalendarItem> hourList = dayMap.get(calendarItem.getStartTime().getHour());
                    hourList.add(calendarItem);
                } else if (calendarItem.getDate().getDayOfWeek().getValue() == i && calendarItem.getRecurrencePattern().equals("weekly")){
                    ArrayList<CalendarItem> hourList = dayMap.get(calendarItem.getStartTime().getHour());
                    hourList.add(calendarItem);
                } else if (calendarItem.getDate().getDayOfWeek().getValue() == i &&
                        calendarItem.getRecurrencePattern().equals("monthly")){

                    // Add monthly recurring items. Unsure as yet how to get them all. Maybe just do it by date, not d
                    // day of week. (So the 1st of every month or the 27th of every month, etc.)
                }
            }
        }

        int[] hours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};

        model.addAttribute("user", user);
        model.addAttribute("title", "Users");
        model.addAttribute("hours", hours);
        model.addAttribute("dayMaps", dayMaps);

        return "user/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model){
        User user = new User();
        model.addAttribute("title", "Add User");
        model.addAttribute(user);

        return "user/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Model model,
                      @Valid @ModelAttribute("newUser") User newUser,
                      Errors errors,
                      @RequestParam String password,
                      @RequestParam String verify,
                      HttpSession session) throws Exception {
        // TODO: Fix issue with validation. Errors not displaying.
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
        if (existsByUsername){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            model.addAttribute("usernameError","That username is taken. Please choose another.");
            return "user/add";
        }
        if (existsByEmail){
            model.addAttribute("title", "Add User");
            model.addAttribute("user", newUser);
            model.addAttribute("emailError","That email is taken. Please choose another.");
            return "user/add";
        }


        String pwHash = HashUtils.getSaltedHash(password);
        newUser.setPwHash(pwHash);
        userDao.save(newUser);
        session.setAttribute("user",newUser.getEmail());

        return "redirect:";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){

        User user = new User();
        model.addAttribute("title","Log In");
        model.addAttribute("user", user);

        return "user/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model,
                        HttpSession session,
                        @ModelAttribute("user") @Valid User user,
                        @RequestParam String password) throws Exception {
        if (userDao.findByUsername(user.getUsername()) != null) {
            User registeredUser = userDao.findByUsername(user.getUsername());

            if (HashUtils.checkPassword(password, registeredUser.getPwHash())) {
                session.setAttribute("user", registeredUser.getEmail());

                return "redirect:";
            }
        }
        // if login fails, return to login screen with error message
        model.addAttribute("title", "Log In");
        model.addAttribute("user", user);
        model.addAttribute("loginError","Username or password did not match any registered user." +
                " Please check them and try again.");

        return "user/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model, HttpSession session){

        // Should this be session.invalidate()?
        session.removeAttribute("user");

        return "redirect:";
    }
}