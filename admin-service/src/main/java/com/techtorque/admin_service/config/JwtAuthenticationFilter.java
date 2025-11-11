package com.techtorque.admin_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Value("${jwt.secret:mysecretkey}")
  private String jwtSecret;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      
      try {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String username = claims.getSubject();
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        if (username != null && roles != null) {
          List<SimpleGrantedAuthority> authorities = roles.stream()
              .map(role -> {
                String roleUpper = role.trim().toUpperCase();
                // Treat SUPER_ADMIN as ADMIN for authorization purposes
                if ("SUPER_ADMIN".equals(roleUpper)) {
                  // Add both SUPER_ADMIN and ADMIN roles
                  return Arrays.asList(
                      new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"),
                      new SimpleGrantedAuthority("ROLE_ADMIN")
                  );
                }
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleUpper));
              })
              .flatMap(List::stream)
              .collect(Collectors.toList());

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(username, null, authorities);

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        logger.warn("JWT token validation failed: " + e.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }
}
