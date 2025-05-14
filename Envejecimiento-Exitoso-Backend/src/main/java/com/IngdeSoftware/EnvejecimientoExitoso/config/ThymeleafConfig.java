package com.IngdeSoftware.EnvejecimientoExitoso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        // 👉 Apunta al dist donde está tu front rebuildable
        resolver.setPrefix("file:C:/Users/alexa/OneDrive/Escritorio/Ing de software/Envejecimiento-Exitoso-Frontend/dist/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCacheable(false); // para ver cambios sin reiniciar
        return resolver;
    }

}
