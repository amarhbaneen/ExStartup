package com.example.StartupExercise.Configs;

import com.example.StartupExercise.Authentication.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

/**
 * Spring Security configuration class to enable JWT authentication and method-level security.
 */
public class SecurityConfig  {
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();  // Instantiate the filter bean
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.requiresChannel(c -> c.requestMatchers("/actuator/**").requiresInsecure());
        http.authorizeHttpRequests(request -> {
            request.requestMatchers(
                    "/Auth/login",
                    "/actuator/**","/swagger-ui/**","/api-docs/**"
            ).permitAll();
            request.anyRequest().authenticated();
        });
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build(); // Return the configured HttpSecurity
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200"));  // Your Angular frontend URL
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));  // Allowed HTTP methods
        corsConfig.setAllowedHeaders(Arrays.asList("*"));  // Allow all headers
        corsConfig.setAllowCredentials(true);  // Allow credentials like cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);  // Apply CORS config to all paths
        return source;
    }


}
