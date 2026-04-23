package com.smartstock.service;

import com.smartstock.dto.UserRegistrationDTO;
import com.smartstock.dto.UserResponseDTO;
import com.smartstock.model.User;
import com.smartstock.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO register(UserRegistrationDTO userRegistrationDTO) {

        Optional<User> existing = userRepository.findByEmail(userRegistrationDTO.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        User user = modelMapper.map(userRegistrationDTO, User.class);

        String hashedPassword = passwordEncoder.encode(userRegistrationDTO.getPassword());
        user.setPassword(hashedPassword);


        user.setRole("OWNER");

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserResponseDTO.class);
    }

    public UserResponseDTO login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password,user.getPassword())) {
                return modelMapper.map(user,UserResponseDTO.class);
            }
        }
        return null;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserResponseDTO findByEmailDTO(String email) {
        // 1. Ask the database for the real Entity
        Optional<User> userOpt = userRepository.findByEmail(email);

        // 2. If found, use ModelMapper to safely convert it to the DTO
        if (userOpt.isPresent()) {
            return modelMapper.map(userOpt.get(), UserResponseDTO.class);
        }

        // 3. Fallback (Though if they just logged in, this shouldn't happen)
        return null;
    }

    public void createStaff(UserRegistrationDTO staffDto, Long warehouseId) {
        User staff = new User();
        staff.setName(staffDto.getName());
        staff.setEmail(staffDto.getEmail());
        // Hash the password for the Munshi
        staff.setPassword(passwordEncoder.encode(staffDto.getPassword()));

        staff.setRole("STAFF");
        staff.setAssignedWarehouseId(warehouseId);

        userRepository.save(staff);
    }
}
