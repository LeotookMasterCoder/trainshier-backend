package com.trainshier.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trainshier.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT authentication filter.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Validates JWT token and injects user data into request.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain filter chain
     * @throws ServletException servlet exception
     * @throws IOException input/output exception
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Authorization header missing\"}");

            return;
        }

        String token = authHeader.substring(7);

        try {

            if (jwtService.isTokenValid(token)) {

                String username =
                        jwtService.extractUsername(token);

                Long userId =
                        jwtService.extractUserId(token);

                String role =
                        jwtService.extractRole(token);

                request.setAttribute(
                        "username",
                        username);

                request.setAttribute(
                        "userId",
                        userId);

                request.setAttribute(
                        "role",
                        role);

                // Set authentication in Spring Security context
                org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication =
                        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        );
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(
                        request,
                        response);

                return;
            }

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write(
                    "{\"error\":\"Token invalid\"}");

        } catch (Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Authentication failed\"}");
        }
    }

    /**
     * Skips authentication for public endpoints.
     *
     * @param request HTTP request
     * @return true if filter should not execute
     */
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String path =
                request.getRequestURI();

        return path.startsWith("/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }
}