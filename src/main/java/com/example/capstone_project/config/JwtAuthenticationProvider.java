package com.example.capstone_project.config;

import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Get username and password
        final String username = authentication.getPrincipal().toString();
        final String password = authentication.getCredentials().toString();

        // Check if user exists in the database
        Optional<User> optionalUser = this.userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            return null;
        }

        // Check if provide the correct password
        final User user = optionalUser.get();
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(user.getId(), null, List.of());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
