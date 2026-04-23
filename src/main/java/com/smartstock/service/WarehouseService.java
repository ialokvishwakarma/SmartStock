package com.smartstock.service;

import com.smartstock.model.User;
import com.smartstock.model.Warehouse;
import com.smartstock.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    public List<Warehouse> getWarehousesByOwner(User owner) {
        return warehouseRepository.findByOwner(owner);
    }

    public Warehouse getWarehouseByIdAndOwner(Long id, User owner) {
        return warehouseRepository.findByIdAndOwner(id, owner).orElse(null);
    }

    public Warehouse saveWarehouse(Warehouse warehouse, User owner) {
        warehouse.setOwner(owner);
        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(Warehouse warehouse) {
        warehouseRepository.delete(warehouse);
    }
}
