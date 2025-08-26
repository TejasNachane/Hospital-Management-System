package com.golu.hello.backend.config;

import com.golu.hello.backend.entity.Role;
import com.golu.hello.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }
        
        if (roleRepository.findByName("DOCTOR").isEmpty()) {
            Role doctorRole = new Role();
            doctorRole.setName("DOCTOR");
            roleRepository.save(doctorRole);
        }
        
        if (roleRepository.findByName("PATIENT").isEmpty()) {
            Role patientRole = new Role();
            patientRole.setName("PATIENT");
            roleRepository.save(patientRole);
        }
        
        System.out.println("Data initialization completed!");
    }
}
