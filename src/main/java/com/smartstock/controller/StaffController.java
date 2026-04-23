package com.smartstock.controller;

import com.smartstock.dto.UserRegistrationDTO;
import com.smartstock.dto.UserResponseDTO;
import com.smartstock.model.User;
import com.smartstock.model.Warehouse;
import com.smartstock.service.UserService;
import com.smartstock.service.WarehouseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StaffController {

    @Autowired
    private UserService userService;

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/staff/add")
    public String showAddStaffForm(Model model, HttpSession session) {
        UserResponseDTO ownerDto = (UserResponseDTO) session.getAttribute("user");
        if (ownerDto == null) return "redirect:/login";

        // Get the Owner's warehouses so they can pick one for the Munshi
        User owner = userService.findById(ownerDto.getId());
        List<Warehouse> warehouses = warehouseService.getWarehousesByOwner(owner);

        model.addAttribute("staffDto", new UserRegistrationDTO());
        model.addAttribute("warehouses", warehouses);
        return "add_staff";
    }

    @PostMapping("/staff/add")
    public String registerStaff(@ModelAttribute UserRegistrationDTO staffDto,
                                @RequestParam Long warehouseId) {
        userService.createStaff(staffDto, warehouseId);
        return "redirect:/dashboard"; // Back to Owner dashboard
    }
}