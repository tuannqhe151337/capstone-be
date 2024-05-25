package com.example.capstone_project.config;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SeedConfiguration {
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner(
            UserRepository userRepository
    ) {
        return args -> {
            // User
            User user1 = User.builder()
                    .username("username1")
                    .password(this.passwordEncoder.encode("password"))
                    .build();

            User user2 = User.builder()
                    .username("username2")
                    .password(this.passwordEncoder.encode("password"))
                    .build();

            User user3 = User.builder()
                    .username("username3")
                    .password(this.passwordEncoder.encode("password"))
                    .build();

            User user4 = User.builder()
                    .username("username4")
                    .password(this.passwordEncoder.encode("password"))
                    .build();

            User user5 = User.builder()
                    .username("username5")
                    .password(this.passwordEncoder.encode("password"))
                    .build();

            userRepository.saveAll(List.of(user1, user2, user3, user4, user5));

        };
    }
}
