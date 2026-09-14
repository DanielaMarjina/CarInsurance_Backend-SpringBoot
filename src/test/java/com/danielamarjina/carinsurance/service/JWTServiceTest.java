package com.danielamarjina.carinsurance.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private static final String SECRET_KEY =
            "my-super-secret-key-that-is-at-least-32-characters-long";

    private static final long EXPIRATION =
            60 * 60 * 1000;

    private final JWTService jwtService =
            new JWTService(SECRET_KEY, EXPIRATION);


    @Test
    void generateToken_shouldReturnToken() {
        String email = "daniela@gmail.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }


    @Test
    void extractEmail_shouldReturnEmailFromToken() {
        String email = "daniela@gmail.com";

        String token = jwtService.generateToken(email);

        String result = jwtService.extractEmail(token);

        assertEquals(email, result);
    }


    @Test
    void isTokenValid_shouldReturnTrue_whenEmailMatchesAndTokenIsNotExpired() {
        String email = "daniela@gmail.com";

        String token = jwtService.generateToken(email);

        UserDetails userDetails =
                User.withUsername(email)
                        .password("password")
                        .roles("EMPLOYEE")
                        .build();

        boolean result = jwtService.isTokenValid(token, userDetails);

        assertTrue(result);
    }


    @Test
    void isTokenValid_shouldReturnFalse_whenEmailDoesNotMatch() {
        String token = jwtService.generateToken("daniela@gmail.com");

        UserDetails userDetails =
                User.withUsername("other@gmail.com")
                        .password("password")
                        .roles("EMPLOYEE")
                        .build();

        boolean result = jwtService.isTokenValid(token, userDetails);

        assertFalse(result);
    }


    @Test
    void isTokenExpired_shouldReturnTrue_whenTokenIsExpired() {
        JWTService expiredJwtService =
                new JWTService(SECRET_KEY, -1000);

        String token =
                expiredJwtService.generateToken("daniela@gmail.com");

        boolean result =
                expiredJwtService.isTokenExpired(token);

        assertTrue(result);
    }
}