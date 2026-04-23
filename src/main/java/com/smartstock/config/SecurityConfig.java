package com.smartstock.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomSuccessHandler successHandler;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/home", "/css/**", "/images/**","/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/staff/add/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers("/product/**").hasAnyRole("OWNER", "STAFF")
                        .requestMatchers("/staff/**").hasRole("STAFF")
                        // All other routes require the user to be logged in
                        .anyRequest().authenticated()
                )
                // 2. Form Login Configuration
                .formLogin(form -> form
                        .loginPage("/login") // Use your custom login HTML
                        .loginProcessingUrl("/login") // Spring intercepts the POST request here
                        .successHandler(successHandler) // Use our custom router
                        .failureUrl("/login?error=true") // Send back on failure
                        .permitAll()
                )
                // 3. Logout Configuration
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
