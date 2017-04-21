package com.horeca.site.security.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordHashingService {

    public static boolean checkIfPlainEqualToHashed(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public static String getHashedFromPlain(String plainTextPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainTextPassword, salt);
    }
}
