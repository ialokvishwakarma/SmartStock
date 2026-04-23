package com.smartstock.controller;

import com.smartstock.dto.UserResponseDTO;
import com.smartstock.model.Product;
import com.smartstock.model.User;
import com.smartstock.model.Warehouse;
import com.smartstock.repository.UserRepository;
import com.smartstock.repository.WarehouseRepository;
import com.smartstock.service.ProductService;
import com.smartstock.service.UserService;
import com.smartstock.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseService warehouseService;

    private UserResponseDTO getSessionUser(HttpSession session) {
        return (UserResponseDTO) session.getAttribute("user");
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        UserResponseDTO user = getSessionUser(session);
        if (user == null) return "redirect:/login";

        // Admin needs to see all Owners and Warehouses
        model.addAttribute("allOwners", userRepository.findByRole("OWNER"));
        model.addAttribute("allWarehouses", warehouseRepository.findAll());
        model.addAttribute("userName", user.getName());
        return "admin_dashboard";
    }

    @GetMapping("/staff/dashboard")
    public String staffDashboard(HttpSession session, Model model) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        model.addAttribute("userName", userDto.getName());

        User realUser = userService.findById(userDto.getId());

        if (realUser != null && realUser.getAssignedWarehouseId() != null) {
            Warehouse warehouse = warehouseService.getWarehouseById(realUser.getAssignedWarehouseId());
            model.addAttribute("warehouse", warehouse);

            // THE FIX: Fetch products explicitly from the database using ProductService
            List<Product> products = productService.getProductsByWarehouse(warehouse);
            model.addAttribute("products", products);
        }

        return "staff_dashboard";
    }
}