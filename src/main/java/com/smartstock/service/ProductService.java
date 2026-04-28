package com.smartstock.service;

import com.smartstock.model.Product;
import com.smartstock.model.Warehouse;
import com.smartstock.repository.ProductRepository;
import com.smartstock.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EmailService emailService;

    public List<Product> getProductsByWarehouse(Warehouse warehouse) {
        return productRepository.findByWarehouse(warehouse);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product, Warehouse warehouse) {
        product.setWarehouse(warehouse);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public boolean isSkuExists(String sku) {
        return productRepository.findBySku(sku).isPresent();
    }

    @Transactional
    public void stockIn(Long id, int amoount) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity()+amoount);
        productRepository.save(product);
    }

    public void stockOut(Long id, int amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if(product.getQuantity()<amount){
            throw new RuntimeException("Insufficient stock! Available: " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);

        // low stock alert notification through email
        if (product.getQuantity() <= product.getMinStockLevel()) {
            String ownerEmail = product.getWarehouse().getOwner().getEmail();
            emailService.sendLowStockAlert(ownerEmail,product.getName(), product.getQuantity(),product.getWarehouse().getName());
        }
    }


}
