package com.project.config;

import com.project.model.User;
import com.project.repository.UserRepository;
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
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@candidatesystem.com")) {
            User admin = new User();
            admin.setEmail("admin@candidatesystem.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Admin");
            admin.setRole(User.Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created - Email: admin@candidatesystem.com, Password: admin123");
        }
    }
}
