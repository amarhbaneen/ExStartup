package com.example.StartupExercise.AuthConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
}
