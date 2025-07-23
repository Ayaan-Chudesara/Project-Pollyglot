package com.pollyglot.pollyglot_backend.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from your Angular frontend to all /api/** endpoints
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200") // IMPORTANT: Replace with your Angular app's actual URL if different
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow sending cookies and authorization headers
    }
}
