package com.project.service;

import com.project.model.User;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("[CustomUserDetailsService] Loading user: " + email + " on thread: " + Thread.currentThread().getName());
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("[CustomUserDetailsService] User NOT FOUND: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        
        System.out.println("[CustomUserDetailsService] User FOUND: " + email + " with role: " + user.getRole());
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
