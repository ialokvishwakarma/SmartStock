package com.smartstock.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String name;
    private String email;
    private String password;

    // for role based
    private String role;
}
