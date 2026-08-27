package com.budgettracker.controller;

import com.budgettracker.model.User;
import com.budgettracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/login"})
    public String login() {
        return "login"; // Resolves to templates/login.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Resolves to templates/register.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Resolves to templates/dashboard.html
    }

    @GetMapping("/dashboard-alt")
    public String dashboardAlt() {
        return "dashboard-alt";
    }

    @GetMapping("/dashboard-chart")
    public String dashboardChart() {
        return "dashboard-chart";
    }

    @GetMapping("/add-budget")
    public String addBudget() {
        return "add-budget";
    }

    @GetMapping("/add-expenses")
    public String addExpenses() {
        return "add-expenses";
    }

    @GetMapping("/archive")
    public String archive() {
        return "archive";
    }

    @GetMapping("/admin-archive")
    public String adminArchive() {
        return "admin-archive";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("user", user);
        }
        return "profile";
    }

    @GetMapping("/admin-users")
    public String adminUsers() {
        return "admin-users";
    }

    @GetMapping("/user-list")
    public String userList() {
        return "user-list";
    }
}