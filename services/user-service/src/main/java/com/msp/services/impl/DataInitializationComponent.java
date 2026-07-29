package com.msp.services.impl;

import com.msp.enums.UserRole;
import com.msp.models.User;
import com.msp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String email = "codewithmsp@gmail.com";
        String password = "codewithmsp";

        if(userRepository.findByEmail(email) == null) {
            User adminUser = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .fullName("Pranav")
                    .role(UserRole.ROLE_SYSTEM_ADMIN)
                    .build();

            User savedAdmin = userRepository.save(adminUser);
        }
    }

}
