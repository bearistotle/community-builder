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

import java.lang.Character;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.validation.Valid;

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
                      Errors errors,
                      @RequestParam String password,
                      @RequestParam String verify){
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

        // password hashing (dummy version to get v.0 of the program up and running.Come back and replace with real
        // password hashing (Spring Security?)
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        try{
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA512");

            byte[] hashBytes = factory.generateSecret(spec).getEncoded();

            StringBuilder sb = new StringBuilder(512);
            for (byte b : hashBytes){
                sb.append(b);
            }
            String pwHash;
            pwHash = sb.toString();
            newUser.setPwHash(pwHash);
            userDao.save(newUser);

        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){

        }

        return "redirect:";
    }
}