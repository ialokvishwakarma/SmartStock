package com.smartstock.repository;

import com.smartstock.model.User;
import com.smartstock.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByOwner(User owner);
    Optional<Warehouse> findByIdAndOwner(Long id, User owner);
}
