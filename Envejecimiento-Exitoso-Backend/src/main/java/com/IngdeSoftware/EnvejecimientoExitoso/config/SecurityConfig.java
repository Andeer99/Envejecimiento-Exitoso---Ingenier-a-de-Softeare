package com.IngdeSoftware.EnvejecimientoExitoso.config;

import com.IngdeSoftware.EnvejecimientoExitoso.service.UsuarioDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UsuarioDetailsService uds;
    private final PasswordEncoder       encoder;
    private final JwtAuthFilter         jwtFilter;

    public SecurityConfig(UsuarioDetailsService uds,
                          PasswordEncoder encoder,
                          JwtAuthFilter jwtFilter) {
        this.uds       = uds;
        this.encoder   = encoder;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) Habilitar CORS y desactivar CSRF
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                // 2) Sesión sin estado
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3) Manejo de errores JSON
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint())
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Acceso denegado\"}");
                        })
                )

                // 4) Reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitir TODOS los pre-flight OPTIONS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints de autenticación bajo /auth/**
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                        // Registro de clientes
                        .requestMatchers(HttpMethod.POST, "/api/clientes", "/api/clientes/**")
                        .permitAll()

                        // Consulta pública de productos
                        .requestMatchers(HttpMethod.GET, "/api/productos/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()

                        // Recursos estáticos y error
                        .requestMatchers("/", "/css/**", "/js/**", "/error")
                        .permitAll()

                        // Resto requieren autenticación
                        .anyRequest().authenticated()
                )

                // 5) Proveedor de autenticación y filtro JWT
                .authenticationProvider(daoAuthProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(uds);
        return provider;
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    private AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (req, res, ex) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"No autorizado\"}");
        };
    }
}
