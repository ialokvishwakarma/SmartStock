package com.smartstock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Warehouse name is required")
    private String name;

    private String location;

    private String capacity;

    private String type;

    // Each warehouse belongs to a single admin (User)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Object getProducts() {
        return null;
    }
}
