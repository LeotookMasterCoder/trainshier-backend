package com.trainshier.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.trainshier.security.RoleInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * Web configuration.
 *
 * @param roleInterceptor role interceptor
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RoleInterceptor roleInterceptor;

    /**
     * Register interceptors.
     *
     * @param registry interceptor registry
     */
    @Override
    public void addInterceptors(
            InterceptorRegistry registry
    ) {

        registry.addInterceptor(
                roleInterceptor
        );
    }
}