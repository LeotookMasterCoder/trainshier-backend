package com.trainshier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import com.trainshier.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration.
 *
 * @param jwtAuthenticationFilter jwt filter
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Security filter chain.
     *
     * @param http http security
     * @return security chain
     * @throws Exception exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .csrf(
                        csrf -> csrf.disable()
                )

                .cors(
                        Customizer.withDefaults()
                )

                .sessionManagement(

                        session -> session

                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )

                .authorizeHttpRequests(

                        auth -> auth

                                .requestMatchers(

                                        "/auth/**",

                                        "/swagger-ui/**",

                                        "/v3/api-docs/**"

                                ).permitAll()

                                .anyRequest()

                                .authenticated()
                )

                .addFilterBefore(

                        jwtAuthenticationFilter,

                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}