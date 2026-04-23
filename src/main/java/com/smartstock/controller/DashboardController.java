package com.smartstock.controller;

import com.smartstock.dto.UserResponseDTO;
import com.smartstock.model.User;
import com.smartstock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    private UserResponseDTO getSessionUser(HttpSession session) {
        return (UserResponseDTO) session.getAttribute("user");
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        UserResponseDTO user = getSessionUser(session);
        if (user == null) return "redirect:/login";

        model.addAttribute("userName", user.getName());
        return "admin_dashboard";
    }

    @GetMapping("/staff/dashboard")
    public String staffDashboard(HttpSession session, Model model) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        model.addAttribute("userName", userDto.getName());

        // Fetch the real user to get their assigned warehouse ID
        User realUser = userService.findById(userDto.getId());
        if (realUser != null && realUser.getAssignedWarehouseId() != null) {
            model.addAttribute("warehouseId", realUser.getAssignedWarehouseId());
        }

        return "staff_dashboard";
    }
}