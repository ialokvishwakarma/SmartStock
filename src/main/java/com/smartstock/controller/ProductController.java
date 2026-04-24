package com.smartstock.controller;

import com.smartstock.dto.UserResponseDTO;
import com.smartstock.model.User;
import com.smartstock.model.Product;
import com.smartstock.model.Warehouse;
import com.smartstock.service.ProductService;
import com.smartstock.service.UserService;
import com.smartstock.service.WarehouseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private UserService userService;

    private UserResponseDTO getSessionUser(HttpSession session) {
        return (UserResponseDTO) session.getAttribute("user");
    }

    private User getRealUser(UserResponseDTO dto) {
        return (dto != null) ? userService.findById(dto.getId()) : null;
    }


    // method to check the role of the person trying of product operations
    private boolean hasAccessToWarehouse(User user, Long targetWarehouseId) {
        if (user == null) return false;
        if ("STAFF".equals(user.getRole())) {
            return targetWarehouseId.equals(user.getAssignedWarehouseId());
        } else if ("OWNER".equals(user.getRole())) {
            Warehouse w = warehouseService.getWarehouseByIdAndOwner(targetWarehouseId, user);
            return w != null;
        }
        return false;
    }

    private String getRedirectRoute(User user, Long warehouseId) {
        if ("STAFF".equals(user.getRole())) {
            return "redirect:/staff/dashboard";
        }
        return "redirect:/products/" + warehouseId;
    }


    //controllers
    @GetMapping("/products/{warehouseId}")
    public String listProducts(@PathVariable Long warehouseId, Model model, HttpSession session) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        if (!hasAccessToWarehouse(realUser, warehouseId)) return "redirect:/login";

        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        List<Product> products = productService.getProductsByWarehouse(warehouse);

        model.addAttribute("warehouse", warehouse);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/product/add/{warehouseId}")
    public String showAddProduct(@PathVariable Long warehouseId, Model model, HttpSession session) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        if (!hasAccessToWarehouse(realUser, warehouseId)) return "redirect:/login";

        model.addAttribute("product", new Product());
        model.addAttribute("warehouseId", warehouseId);
        return "add_product";
    }

    @PostMapping("/product/add/{warehouseId}")
    public String addProduct(@PathVariable Long warehouseId,
                             @ModelAttribute Product product,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        if (!hasAccessToWarehouse(realUser, warehouseId)) return "redirect:/login";

        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);

        if (productService.isSkuExists(product.getSku())) {
            redirectAttributes.addFlashAttribute("error", "SKU already exists!");
        } else {
            productService.saveProduct(product, warehouse);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        }

        return getRedirectRoute(realUser, warehouseId);
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model, HttpSession session) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        Product product = productService.getProductById(id);
        User realUser = getRealUser(userDto);

        if (!hasAccessToWarehouse(realUser, product.getWarehouse().getId())) return "redirect:/login";

        model.addAttribute("product", product);
        model.addAttribute("warehouseId", product.getWarehouse().getId());
        return "edit_product";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        Product existing = productService.getProductById(id);
        User realUser = getRealUser(userDto);

        if (!hasAccessToWarehouse(realUser, existing.getWarehouse().getId())) return "redirect:/login";

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        productService.saveProduct(existing, existing.getWarehouse());

        redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        return getRedirectRoute(realUser, existing.getWarehouse().getId());
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        Product product = productService.getProductById(id);
        Long warehouseId = product.getWarehouse().getId();
        User realUser = getRealUser(userDto);

        if (!hasAccessToWarehouse(realUser, warehouseId)) return "redirect:/login";

        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted!");

        return getRedirectRoute(realUser, warehouseId);
    }
}