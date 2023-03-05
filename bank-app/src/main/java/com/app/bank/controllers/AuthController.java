package com.app.bank.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    @GetMapping("login")
    public String getLogin(HttpServletRequest request, Model model) {
        return "login";
    }
}