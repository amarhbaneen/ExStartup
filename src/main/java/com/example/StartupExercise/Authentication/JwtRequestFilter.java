package com.example.StartupExercise.Authentication;

import com.example.StartupExercise.User.User;
import com.example.StartupExercise.User.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    // Injecting UserRepository
    public JwtRequestFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;
        String role = null;

        // Extract the JWT token from the "Authorization" header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);  // Remove "Bearer " prefix
            username = JwtUtil.extractUsername(jwtToken);

            // Ensure you are extracting the role from the token properly
            role = Jwts.parser()
                    .setSigningKey(JwtUtil.JWT_SECRET)
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .get("role", String.class);
            System.out.println("Extracted Role: " + role);
        }

        // If username exists in the token and no authentication exists, set authentication context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Fetch authorities for the user
            List<SimpleGrantedAuthority> authorities = getAuthoritiesForUser(username);
            System.out.println("Extracted Authorities: " + authorities);
            // Create the authentication object with authorities
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);  // Continue with the filter chain
    }

    private List<SimpleGrantedAuthority> getAuthoritiesForUser(String username) {
        // Fetch the user from the database (or any data source)
        User user = userRepository.findByUsername(username);

        // Convert roles to authorities (ensure ROLE_ prefix is included)
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}
