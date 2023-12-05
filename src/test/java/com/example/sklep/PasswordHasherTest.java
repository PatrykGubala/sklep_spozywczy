package com.example.sklep;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    void testHashAndMatch() {
        PasswordHasher passwordHasher = new PasswordHasher();

        // Test hashing and matching with a valid password
        String rawPassword = "SecurePassword123";
        String hashedPassword = passwordHasher.hash(rawPassword);
        assertTrue(passwordHasher.matches(rawPassword, hashedPassword));
    }

    @Test
    void testMismatchedPasswords() {
        PasswordHasher passwordHasher = new PasswordHasher();

        // Test matching with mismatched passwords
        String rawPassword = "Password123";
        String incorrectPassword = "WrongPassword456";
        String hashedPassword = passwordHasher.hash(rawPassword);
        assertFalse(passwordHasher.matches(incorrectPassword, hashedPassword));
    }

    @Test
    void testEmptyPassword() {
        PasswordHasher passwordHasher = new PasswordHasher();

        // Test hashing and matching with an empty password
        String rawPassword = "";
        String hashedPassword = passwordHasher.hash(rawPassword);
        assertTrue(passwordHasher.matches(rawPassword, hashedPassword));
    }

    @Test
    void testNullPassword() {
        PasswordHasher passwordHasher = new PasswordHasher();
        assertThrows(NullPointerException.class, () -> {
            passwordHasher.hash(null);
        });
    }
}
