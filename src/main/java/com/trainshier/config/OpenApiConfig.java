package com.trainshier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Swagger configuration.
 */
@Configuration
public class OpenApiConfig {

    /**
     * OpenAPI configuration.
     *
     * @return open api instance
     */
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(

                        new Info()

                                .title("TrainShier API")

                                .version("1.0.0")

                                .description(
                                        "TrainShier Backend API"
                                )
                );
    }

}