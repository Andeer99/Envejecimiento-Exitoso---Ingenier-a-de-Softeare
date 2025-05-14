package com.IngdeSoftware.EnvejecimientoExitoso.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe: " + email));
        return User.builder()
                .username(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRoles().toArray(new String[0]))
                .build();
    }
}
