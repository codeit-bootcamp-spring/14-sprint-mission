package com.sprint.mission.discodeit.global.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordEncoderTest {

    private final PasswordEncoder encoder = new SecurityConfig().passwordEncoder();

    @Test
    void matchesNewAndExistingBcryptPasswords() {
        String password = "test-password";
        String newHash = encoder.encode(password);
        String existingHash = new BCryptPasswordEncoder().encode(password);

        assertTrue(newHash.startsWith("{bcrypt}"));
        assertTrue(encoder.matches(password, newHash));
        assertTrue(encoder.matches(password, existingHash));
        assertFalse(encoder.matches("wrong-password", existingHash));
    }
}
