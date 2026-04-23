package com.smartstock.service;

import com.smartstock.model.Product;
import com.smartstock.model.Warehouse;
import com.smartstock.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

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
}
