package com.IngdeSoftware.EnvejecimientoExitoso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Permite dominios de tu frontend (Railway y local)
        config.setAllowedOrigins(List.of(
                "https://envejecimiento-exitoso-production.up.railway.app",
                "http://localhost:5173"
        ));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a todas las rutas del backend
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
