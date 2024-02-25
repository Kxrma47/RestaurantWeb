package com.example.restaurant.api.securityModul;

import com.example.restaurant.api.entities.User;
import com.example.restaurant.api.services.UserService;
import com.example.restaurant.api.util.Saver;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RequestMapping("/security")
@Controller
public class Security {
    @Autowired
    UserService userService;
    private static boolean auth = false;
    private static String current = "";
    @Getter
    private static User currentUser = null;
    @Autowired
    Saver saver;
    @GetMapping("/registration")
    public String registration(Model model) {
        if (saver != null) {
            saver.createFiles();
            Saver.saveIncome();
            saver.read();
            saver = null;
        }
        if (!auth) {
            model.addAttribute("userForm", new User());
            return "security/registration";
        } else if (current.equals("user")){
            return "redirect:/user/userDashboard";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/registration")
    private String registration(@ModelAttribute("userForm") User userForm, Model model) {
        if (!auth) {
            if (!userService.checkForRegistration(userForm)) {
                userService.addPerson(new User(userForm.getLogin(), userForm.getPassword(), new ArrayList<>()));
                current = "user";
                return "security/login";
            }
        }
        model.addAttribute("error", "Invalid login or password");
        return "security/registration";
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (auth) {
            if (current.equals("user")) {
                return "redirect:/user/userDashboard";
            } else {
                return "redirect:/admin/dashboard";
            }
        }
        model.addAttribute("userForm", new User());
        return "security/login";
    }
    @PostMapping ("/login")
    private String login(@ModelAttribute("userForm") User userForm, Model model) {
        if (!auth) {
            if (userForm.getLogin().equals("admin") && userForm.getPassword().equals("admin")) {
                auth = true;
                current = "admin";
                return "redirect:/admin/dashboard";
            } else if (userService.checkForLogin(userForm)){
                    auth = true;
                    current = "user";
                    currentUser = userService.findByNameAndPassword(userForm);
                    return "redirect:/user/userDashboard";
            }
        } else if (current.equals("user")) {
            return "redirect:/user/userDashboard";
        } else {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid login or password");

        return "security/login";
    }

    @GetMapping("/")
    private String exit() {
        auth = false;
        current = "";
        currentUser = null;
        return "redirect:/security/registration";
    }

    public static boolean checkForAuth() {
        return auth;
    }
    public static String checkForCurrentAcc() {
        return current;
    }
}
