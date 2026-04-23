package com.smartstock.controller;

import com.smartstock.dto.UserResponseDTO; // Import your DTO
import com.smartstock.model.User;
import com.smartstock.model.Warehouse;
import com.smartstock.service.UserService; // Needed to fetch the real Entity
import com.smartstock.service.WarehouseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private UserService userService;


    private UserResponseDTO getUserFromSession(HttpSession session) {
        return (UserResponseDTO) session.getAttribute("user");
    }


    private User getRealUser(UserResponseDTO dto) {
        if (dto == null) return null;
        return userService.findById(dto.getId());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UserResponseDTO userDto = getUserFromSession(session);
        if (userDto == null) return "redirect:/login";


        User realUser = getRealUser(userDto);

        List<Warehouse> warehouses = warehouseService.getWarehousesByOwner(realUser);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("userName", userDto.getName());
        return "dashboard";
    }

    @GetMapping("/warehouse/add")
    public String showAddForm(Model model, HttpSession session) {
        if (getUserFromSession(session) == null) return "redirect:/login";
        model.addAttribute("warehouse", new Warehouse());
        return "add_warehouse";
    }

    @PostMapping("/warehouse/add")
    public String addWarehouse(@ModelAttribute Warehouse warehouse,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        UserResponseDTO userDto = getUserFromSession(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        warehouseService.saveWarehouse(warehouse, realUser);

        redirectAttributes.addFlashAttribute("success", "Warehouse added successfully!");
        return "redirect:/dashboard";
    }

    @GetMapping("/warehouse/edit/{id}")
    public String editWarehouse(@PathVariable Long id, Model model, HttpSession session) {
        UserResponseDTO userDto = getUserFromSession(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        Warehouse warehouse = warehouseService.getWarehouseByIdAndOwner(id, realUser);
        model.addAttribute("warehouse", warehouse);
        return "edit_warehouse";
    }

    @PostMapping("/warehouse/update/{id}")
    public String updateWarehouse(@PathVariable Long id,
                                  @ModelAttribute Warehouse warehouse,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        UserResponseDTO userDto = getUserFromSession(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        Warehouse existing = warehouseService.getWarehouseByIdAndOwner(id, realUser);

        if (existing != null) {
            existing.setName(warehouse.getName());
            existing.setType(warehouse.getType());
            existing.setLocation(warehouse.getLocation());
            existing.setCapacity(warehouse.getCapacity());
            warehouseService.saveWarehouse(existing, realUser);
        }

        redirectAttributes.addFlashAttribute("success", "Warehouse updated!");
        return "redirect:/dashboard";
    }

    @GetMapping("/warehouse/delete/{id}")
    public String deleteWarehouse(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDTO userDto = getUserFromSession(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        Warehouse warehouse = warehouseService.getWarehouseByIdAndOwner(id, realUser);

        if (warehouse != null) {
            warehouseService.deleteWarehouse(warehouse);
            redirectAttributes.addFlashAttribute("success", "Warehouse deleted!");
        }

        return "redirect:/dashboard";
    }
}