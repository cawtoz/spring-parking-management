package com.github.cawtoz.parking.controller;

import com.github.cawtoz.parking.model.User;
import com.github.cawtoz.parking.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {

        String successMessage = (String) session.getAttribute("success");
        String errorMessage = (String) session.getAttribute("error");

        if (successMessage != null) {
            model.addAttribute("success", successMessage);
            session.removeAttribute("success");
        }

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
            session.removeAttribute("error");
        }

        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.register(user);
        redirectAttributes.addFlashAttribute("success", "Registro completado. Inicia sesión");
        return "redirect:/login";
    }

}
