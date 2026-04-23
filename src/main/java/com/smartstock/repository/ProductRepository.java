package com.smartstock.repository;

import com.smartstock.model.Product;
import com.smartstock.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByWarehouse(Warehouse warehouse);
    Optional<Product> findBySku(String sku);
}
