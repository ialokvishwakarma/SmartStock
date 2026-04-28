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

        // fetch the email of the person who login throghg the common login page
        String email = authentication.getName();


        // create dto data and put in the session for controllers
        UserResponseDTO userDto = userService.findByEmailDTO(email);
        request.getSession().setAttribute("user", userDto);

        // find out their assigned role
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // handles where to redirect acc. to roles
        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");//admin
        } else if (role.equals("ROLE_STAFF")) {
            response.sendRedirect("/staff/dashboard");//staff
        } else {
            response.sendRedirect("/dashboard"); //warehouse owner
        }
    }
}