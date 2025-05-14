package com.IngdeSoftware.EnvejecimientoExitoso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Cualquier petición “/**” se mapea al directorio dist de tu front
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "file:C:/Users/alexa/OneDrive/Escritorio/Ing de software/Envejecimiento-Exitoso-Frontend/dist/");
    }
    @Controller
    public static class SpaController {
        @GetMapping("/login")
        public String login() {
            return "forward:/login.html";
        }
    }


}
