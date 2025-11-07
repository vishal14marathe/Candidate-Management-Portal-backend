package com.project.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestUri = request.getRequestURI();
        String threadName = Thread.currentThread().getName();
        
        System.out.println("[JwtFilter] === START === Thread: " + threadName + " | URI: " + requestUri);
        
        // Always start with a clean security context for this request
        SecurityContextHolder.clearContext();
        
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                System.out.println("[JwtFilter] Valid JWT for user: " + username + " | Thread: " + threadName);
                
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication ONLY for this thread/request
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    System.out.println("[JwtFilter] AUTH SUCCESS: " + username + " with roles: " + userDetails.getAuthorities() + " | Thread: " + threadName);
                } catch (org.springframework.security.core.userdetails.UsernameNotFoundException ex) {
                    // User was deleted - send 401 response immediately
                    System.out.println("[JwtFilter] USER NOT FOUND (DELETED): " + username + " | Thread: " + threadName);
                    logger.warn("User not found (possibly deleted): " + username);
                    
                    // Send 401 Unauthorized response with JSON body
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(
                        "{\"error\":\"Account not found\",\"message\":\"Your account has been deleted or is no longer accessible\"}"
                    );
                    response.getWriter().flush();
                    System.out.println("[JwtFilter] === END (401) === Thread: " + threadName);
                    return; // Stop filter chain execution - don't process this request further
                }
            } else {
                System.out.println("[JwtFilter] No valid JWT token | Thread: " + threadName);
            }
        } catch (Exception ex) {
            System.out.println("[JwtFilter] EXCEPTION: " + ex.getMessage() + " | Thread: " + threadName);
            logger.error("Could not set user authentication in security context", ex);
            SecurityContextHolder.clearContext();
        }

        try {
            // Continue filter chain
            System.out.println("[JwtFilter] Continuing filter chain | Thread: " + threadName);
            filterChain.doFilter(request, response);
        } finally {
            // Always clear context after request completes to prevent any cross-contamination
            SecurityContextHolder.clearContext();
            System.out.println("[JwtFilter] === END (Context Cleared) === Thread: " + threadName);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
