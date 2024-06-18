package com.example.capstone_project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfiguration {
    @Value("${application.frontend-host}")
    private String FRONTEND_HOST;

    public GlobalCorsConfiguration() {
        super();
    }

    /**
     * Bean to define global CORS.
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins(FRONTEND_HOST)
                        .allowedHeaders("*");
            }
        };
    }
}