package com.ghassen.gymbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*");
        //.allowCredentials(true);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // CETTE LIGNE EST LA SEULE QUI MARCHE À 100% EN 2025
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/uploads/")  // ← UN SEUL SLASH APRÈS file:
                .setCachePeriod(0);

        // Bonus : on ajoute aussi le pattern générique
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/uploads/");
    }


}