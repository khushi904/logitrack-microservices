package org.logitrack.authservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.logitrack.authservice.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");
        log.debug("JWT filter called for {} {}", request.getMethod(), request.getRequestURI());
        log.debug("Authorization header present: {}", authorizationHeader != null && !authorizationHeader.isBlank());

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {
            log.debug("No valid Bearer Authorization header found");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        log.debug("Bearer token received; token length={}", token.length());

        if (jwtService.isTokenValid(token)) {

            String userId = jwtService.extractUserId(token);
            String role = jwtService.extractRole(token);

            log.debug("JWT validated successfully. userId={}, role={}", userId, role);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            log.debug("SecurityContext authentication set. authenticated={}",
                    SecurityContextHolder.getContext().getAuthentication() != null);
        } else {
            log.debug("JWT validation failed");
        }

        filterChain.doFilter(request, response);
    }
}