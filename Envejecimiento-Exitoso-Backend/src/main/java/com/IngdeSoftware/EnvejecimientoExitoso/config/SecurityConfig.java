package com.IngdeSoftware.EnvejecimientoExitoso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // habilita CORS usando tu CorsFilter
                .cors(Customizer.withDefaults())

                // deshabilita CSRF con la nueva DSL
                .csrf(csrf -> csrf.disable())

                // tus reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
