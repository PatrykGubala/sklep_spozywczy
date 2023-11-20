package com.example.sklep;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    private static final Argon2 argon2 = Argon2Factory.create();
    private static final int SALT_LENGTH = 16; // You can adjust the salt length
    private static final String PEPPER = "pepper.pl"; // Choose a secret pepper string

    private byte[] salt;

    public byte[] getSalt() {
        return salt;
    }

    public String hash(String password) {
        // Generate a random salt only if it's not set
        if (salt == null) {
            salt = generateSalt();
        }

        // Concatenate pepper + password + salt
        String pepperedPassword = PEPPER + password + Base64.getEncoder().encodeToString(salt);

        // Hash the peppered password
        String hash = argon2.hash(22, 65536, 1, pepperedPassword);

        return hash;
    }

    public boolean matches(String rawPassword, String hashedPassword, byte[] salt) {
        // Use the stored salt for password verification
        String pepperedPassword = PEPPER + rawPassword + Base64.getEncoder().encodeToString(salt);
        return argon2.verify(hashedPassword, pepperedPassword);
    }

    private byte[] generateSalt() {
        // Generate a random salt using SecureRandom
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
