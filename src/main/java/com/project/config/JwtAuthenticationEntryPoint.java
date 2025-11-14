package com.project.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        // Send 401 Unauthorized response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String message = authException.getMessage();
        if (message == null || message.isEmpty()) {
            message = "Unauthorized access - please login";
        }
        
        response.getWriter().write(
            String.format("{\"error\":\"Unauthorized\",\"message\":\"%s\"}", message)
        );
        response.getWriter().flush();
    }
}
