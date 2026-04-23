package com.smartstock.config;



import com.smartstock.dto.UserResponseDTO;
import com.smartstock.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. Get the email of the person who just logged in
        String email = authentication.getName();

        // 2. Fetch your DTO and put it in the session for your existing Controllers
        UserResponseDTO userDto = userService.findByEmailDTO(email);
        request.getSession().setAttribute("user", userDto);

        // 3. Find out their role
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // 4. Redirect based on the Munshi hierarchy
        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (role.equals("ROLE_STAFF")) {
            response.sendRedirect("/staff/dashboard");
        } else {
            response.sendRedirect("/dashboard"); // Owner dashboard
        }
    }
}