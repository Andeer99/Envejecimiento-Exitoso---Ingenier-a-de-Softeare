package com.IngdeSoftware.EnvejecimientoExitoso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    /** Carpeta física donde se guardan las imágenes (relativa al JAR/contendor). */
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;             // se leerá de application.properties

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  /uploads/xxx.jpg  →  file:<uploadDir>/xxx.jpg
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/")
                .setCachePeriod(3600);
    }
}
