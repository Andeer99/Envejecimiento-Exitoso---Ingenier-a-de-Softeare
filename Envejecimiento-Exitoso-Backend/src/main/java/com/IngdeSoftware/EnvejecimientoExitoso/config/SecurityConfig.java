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
    private final PasswordEncoder       encoder;
    private final JwtAuthFilter         jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authMgr) throws Exception {
        http
                // 1) Stateless, disable CSRF
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2) JSON error responses
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint())
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Acceso denegado\"}");
                        })
                )

                // 3) Route authorization
                .authorizeHttpRequests(auth -> auth
                        // login / refresh
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        // register cliente
                        .requestMatchers(HttpMethod.POST,
                                "/api/clientes",
                                "/api/clientes/**").permitAll()
                        // public catalog
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        // error path
                        .requestMatchers("/error").permitAll()
                        // static resources
                        .requestMatchers("/", "/css/**", "/js/**").permitAll()
                        // all others need authentication
                        .anyRequest().authenticated()
                )

                // 4) authentication provider + JWT filter
                .authenticationProvider(daoAuthProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setPasswordEncoder(encoder);
        p.setUserDetailsService(uds);
        return p;
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
