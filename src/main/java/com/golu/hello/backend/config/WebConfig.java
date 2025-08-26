package com.golu.hello.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(
                    "http://localhost:3000",     // React development server
                    "http://127.0.0.1:3000",     // Alternative localhost
                    "http://localhost:3001",     // Alternative React port
                    "http://localhost:5173",     // Vite development server
                    "http://localhost:4200",     // Angular development server
                    "https://yourdomain.com",    // Production domain (update as needed)
                    "https://*.yourdomain.com"   // Production subdomains (update as needed)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Content-Disposition")
                .maxAge(3600);
    }
}
