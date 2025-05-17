package com.IngdeSoftware.EnvejecimientoExitoso.config;

import com.IngdeSoftware.EnvejecimientoExitoso.service.UsuarioDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public SecurityConfig(UsuarioDetailsService uds, PasswordEncoder encoder, JwtAuthFilter jwtFilter) {
        this.uds = uds;
        this.encoder = encoder;
        this.jwtFilter = jwtFilter;
    }

    private final UsuarioDetailsService uds;
    private final PasswordEncoder encoder;
    private final JwtAuthFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authManager) throws Exception {
        http

                // 1) JWT stateless → no sesiones, CSRF off
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2) Errores JSON en vez de página de login Spring
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint())
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        res.setContentType("application/json");
                        res.getWriter().write("{\"error\":\"Acceso Denegado\"}");})
                )

                // 3) Configuración de rutas públicas y protegidas
                .authorizeHttpRequests(auth -> auth
                        // LOGIN y REFRESHTOKEN
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                        // REGISTRO de clientes
                        .requestMatchers(HttpMethod.POST, "/api/clientes", "/api/clientes/", "/api/clientes/**").permitAll()

                        // Catálogo público de productos
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()

                        // Archivos estáticos / páginas sueltas
                        .requestMatchers("/", "/css/**", "/js/**", "/login.html", "/registro.html")
                        .permitAll()

                        // Todo lo demás requiere JWT válido
                        .anyRequest().authenticated()

                )

                // 4) Provider y filtro JWT
                .authenticationProvider(authProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setPasswordEncoder(encoder);
        p.setUserDetailsService(uds);

        return p;
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /** Devuelve 401 con JSON en lugar de redirect a login form */
    private AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (req, res, ex) -> {
            res.setContentType("application/json");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"No autorizado\"}");
        };
    }

    /** Devuelve 403 con JSON en caso de acceso denegado */
    private AccessDeniedHandler restAccessDeniedHandler() {
        return (req, res, ex) -> {
            res.setContentType("application/json");
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().write("{\"error\":\"Acceso denegado\"}");
        };
    }
}
