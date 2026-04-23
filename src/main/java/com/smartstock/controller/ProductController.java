package com.smartstock.controller;

import com.smartstock.dto.UserResponseDTO; // Important: Import your DTO
import com.smartstock.model.User;
import com.smartstock.model.Product;
import com.smartstock.model.Warehouse;
import com.smartstock.service.ProductService;
import com.smartstock.service.UserService; // Inject this
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

    @GetMapping("/products/{warehouseId}")
    public String listProducts(@PathVariable Long warehouseId, Model model, HttpSession session) {
        UserResponseDTO userDto = getSessionUser(session);
        if (userDto == null) return "redirect:/login";

        User realUser = getRealUser(userDto);
        Warehouse warehouse = warehouseService.getWarehouseByIdAndOwner(warehouseId, realUser);

        List<Product> products = productService.getProductsByWarehouse(warehouse);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/product/add/{warehouseId}")
    public String showAddProduct(@PathVariable Long warehouseId, Model model, HttpSession session) {
        if (getSessionUser(session) == null) return "redirect:/login";

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
        Warehouse warehouse = warehouseService.getWarehouseByIdAndOwner(warehouseId, realUser);

        if (productService.isSkuExists(product.getSku())) {
            redirectAttributes.addFlashAttribute("error", "SKU already exists!");
        } else {
            productService.saveProduct(product, warehouse);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        }

        return "redirect:/products/" + warehouseId;
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model, HttpSession session) {
        if (getSessionUser(session) == null) return "redirect:/login";

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("warehouseId", product.getWarehouse().getId());
        return "edit_product";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (getSessionUser(session) == null) return "redirect:/login";

        Product existing = productService.getProductById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        productService.saveProduct(existing, existing.getWarehouse());

        redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        return "redirect:/products/" + existing.getWarehouse().getId();
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (getSessionUser(session) == null) return "redirect:/login";

        Product product = productService.getProductById(id);
        Long warehouseId = product.getWarehouse().getId();
        productService.deleteProduct(id);

        redirectAttributes.addFlashAttribute("success", "Product deleted!");
        return "redirect:/products/" + warehouseId;
    }
}