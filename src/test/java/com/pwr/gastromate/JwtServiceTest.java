package com.pwr.gastromate;
import com.pwr.gastromate.config.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Inicjalizacja mocków
    }

    @Test
    public void testIsRefreshTokenValid_withValidToken() {
        // Generowanie poprawnego tokena
        String validToken = Jwts.builder()
                .claims(Map.of("sub", "test@example.com"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // Ważny przez 5 minut
                .signWith(getSignKey())
                .compact();

        // Testowanie poprawnego tokena
        assertTrue(jwtService.isRefreshTokenValid(validToken), "Valid token should return true");
    }

    @Test
    public void testIsRefreshTokenValid_withExpiredToken() {
        // Generowanie wygasłego tokena
        String expiredToken = Jwts.builder()
                .claims(Map.of("sub", "test@example.com"))
                .issuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10)) // Wydany 10 minut temu
                .expiration(new Date(System.currentTimeMillis() - 1000 * 60 * 5)) // Wygasł 5 minut temu
                .signWith(getSignKey())
                .compact();

        // Testowanie wygasłego tokena
        assertFalse(jwtService.isRefreshTokenValid(expiredToken), "Expired token should return false");
    }

    @Test
    public void testIsRefreshTokenValid_withInvalidToken() {
        // Nieprawidłowy token
        String invalidToken = "invalid.token.here";

        // Testowanie nieprawidłowego tokena
        assertFalse(jwtService.isRefreshTokenValid(invalidToken), "Invalid token should return false");
    }

    @Test
    public void testIsRefreshTokenValid_withMalformedToken() {
        // Uszkodzony token
        String malformedToken = "eyJhbGciOiJIUzI1NiJ9.malformed.payload";

        // Testowanie uszkodzonego tokena
        assertFalse(jwtService.isRefreshTokenValid(malformedToken), "Malformed token should return false");
    }
}