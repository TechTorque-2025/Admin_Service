package com.techtorque.admin_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // A more comprehensive whitelist for Swagger/OpenAPI, based on the auth-service config.
    private static final String[] SWAGGER_WHITELIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/api-docs/**"
    };

    // Public endpoints accessible by all authenticated users (including CUSTOMER role)
    private static final String[] PUBLIC_ENDPOINTS = {
        "/public/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection for stateless APIs
            .csrf(csrf -> csrf.disable())

            // Set session management to STATELESS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Explicitly disable form login and HTTP Basic authentication
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // Set up authorization rules
            .authorizeHttpRequests(authz -> authz
                // Permit all requests to the Swagger UI and API docs paths
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                
                // Allow authenticated users to access public endpoints
                .requestMatchers(PUBLIC_ENDPOINTS).authenticated()
                
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )

            // Add JWT filter first (for direct service-to-service calls with JWT)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // Add our custom filter to read headers from the Gateway (for gateway-routed calls)
            .addFilterAfter(new GatewayHeaderFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }
}
