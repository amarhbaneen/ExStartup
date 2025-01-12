package com.example.StartupExercise.Authentication;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filter to intercept incoming requests and validate the JWT token.
 * It sets the user authentication if the token is valid.
 */

public class JwtRequestFilter extends OncePerRequestFilter {
    @Override

    /**
     * This method filters each HTTP request to extract and validate the JWT token.
     * If the token is valid, it sets the authentication context for the user.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @param chain    The filter chain.
     * @throws ServletException If the filter encounters a servlet-specific error.
     * @throws IOException      If an I/O error occurs.
     */ protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        // Extract the JWT token from the "Authorization" header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);  // Remove "Bearer " prefix
            username = JwtUtil.extractUsername(jwtToken);
        }

        // If username exists in the token and no authentication exists, set authentication context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // You may want to fetch roles or authorities here for better security
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);  // Continue with the filter chain
    }
}
