package com.app.bank.controllers;

import com.app.bank.dto.User;
import com.app.bank.exceptions.UserException;
import com.app.bank.service.UserServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    @Autowired
    UserServiceLayer userServiceLayer;

    @GetMapping("register")
    public String getRegister(HttpServletRequest request, Model model) {
        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "register";
    }

    @PostMapping("registerUser")
    public String registerUser(HttpServletRequest request, RedirectAttributes attributes) {
        try {
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = "user";

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);

            userServiceLayer.addUser(user);

            attributes.addAttribute("status", "success");
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        return "redirect:/register";
    }

    @GetMapping("login")
    public String getLogin(HttpServletRequest request, Model model) {
        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "login";
    }

    @PostMapping("loginUser")
    public String loginUser(HttpServletRequest request, RedirectAttributes attributes) {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = userServiceLayer.getUserByEmail(email);
            if (user.getPassword().equals(password) && user.getRole().equalsIgnoreCase("user")) {
                attributes.addFlashAttribute("userId", user.getId());
                attributes.addAttribute("status", "success");

                return "redirect:/";
            }
            else {
                throw new UserException("Login failed. Please ensure that your email and password are correct.");
            }
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);

            return "redirect:/login";
        }
    }

    @PostMapping("loginAdmin")
    public String loginAdmin(HttpServletRequest request, RedirectAttributes attributes) {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = userServiceLayer.getUserByEmail(email);
            if (user.getPassword().equals(password) && user.getRole().equalsIgnoreCase("admin")) {
                attributes.addFlashAttribute("userId", user.getId());
                attributes.addAttribute("status", "success");

                return "redirect:/admin";
            }
            else {
                throw new UserException("Login failed. Please ensure that your email and password are correct.");
            }
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);

            return "redirect:/login";
        }
    }
}