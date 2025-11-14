package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@EnableScheduling
public class CandidateManagementApplication {
    public static void main(String[] args) {
        // Set SecurityContextHolder strategy to ensure thread-local isolation
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);
        SpringApplication.run(CandidateManagementApplication.class, args);
    }
}
