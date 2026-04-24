package com.smartstock.config;

import com.smartstock.model.User;
import com.smartstock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@smartstock.com").isEmpty()) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@smartstock.com");
            admin.setPassword(passwordEncoder.encode("Admin123")); //password will store in hashed
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Global Admin saved successfully.");
        }
    }
}