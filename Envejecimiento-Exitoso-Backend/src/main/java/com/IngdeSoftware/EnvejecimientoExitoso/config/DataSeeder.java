package com.IngdeSoftware.EnvejecimientoExitoso.config;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Role;
import com.IngdeSoftware.EnvejecimientoExitoso.model.RoleName;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepo;

    @Override
    public void run(String... args) {
        for (RoleName rn : RoleName.values()) {
            roleRepo.findByName(rn)
                    .orElseGet(() -> roleRepo.save(new Role(null, rn)));
        }
    }
}
