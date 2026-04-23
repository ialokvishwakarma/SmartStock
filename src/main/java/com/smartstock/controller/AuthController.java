package com.smartstock.controller;

import com.smartstock.dto.UserRegistrationDTO;
import com.smartstock.dto.UserResponseDTO;
import com.smartstock.model.User;
import com.smartstock.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO()); // New way
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDTO registrationDTO, Model model) {
        try {
            userService.register(registrationDTO);
            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "login";
    }

//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute("user") UserRegistrationDTO loginDto, HttpSession session, Model model) {
//        UserResponseDTO loggedInUser = userService.login(loginDto.getEmail(),loginDto.getPassword());
//
//        if (loggedInUser != null) {
//            session.setAttribute("user", loggedInUser);
//            return "redirect:/dashboard";
//        } else {
//            model.addAttribute("error", "Invalid credentials! Try again.");
//            return "login";
//        }
//    }

//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/home";
//    }
}
