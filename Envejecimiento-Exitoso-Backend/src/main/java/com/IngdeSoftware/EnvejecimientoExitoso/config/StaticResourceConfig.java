// src/main/java/com/IngdeSoftware/EnvejecimientoExitoso/config/StaticResourceConfig.java
package com.IngdeSoftware.EnvejecimientoExitoso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    // Ruta absoluta donde subes las imágenes (ajústala en application.properties)
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*  /uploads/rosas.jpg  ->  file:/…/uploads/rosas.jpg   */
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/")
                .setCachePeriod(3600);        // 1 h de caché
    }
}
