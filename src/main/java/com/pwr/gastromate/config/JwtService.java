package com.pwr.gastromate.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Próba pobrania claims
        return claimsResolver.apply(claims);

    }

    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Odczyt ról z pola "roles"
    }

    private Claims extractAllClaims(String token) {
        try {
            System.out.println("Parsing token: " + token);
            Claims claims = Jwts.parser()
                    .verifyWith(getSignKey()) // Weryfikacja podpisu
                    .build()
                    .parseSignedClaims(token) // Parsowanie tokena
                    .getPayload(); // Pobranie claims
            System.out.println("Parsed claims: " + claims);
            return claims;
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired, returning expired claims: " + e.getClaims());
            throw e;
        } catch (Exception e) {
            System.out.println("Token parsing or verification failed: " + e.getMessage());
            e.printStackTrace(); // Pełny ślad błędu
            throw new RuntimeException("Invalid or malformed token", e);
        }
    }

    public boolean isAccessToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String type = claims.get("type", String.class);
            return "access".equals(type);
        } catch (Exception e) {
            System.out.println("Error determining token type: " + e.getMessage());
            return false;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of(
                "roles", userDetails.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .toList(),
                "type", "access" // Ustawianie typu tokena na "access"
        );
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minut
                .signWith(getSignKey(), io.jsonwebtoken.SignatureAlgorithm.HS384)
                .compact();
    }



    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of(
                "type", "refresh" // Ustawianie typu tokena na "refresh"
        );
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 dni
                .signWith(getSignKey(), io.jsonwebtoken.SignatureAlgorithm.HS384)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final Claims claims = extractAllClaims(token);
            String username = claims.getSubject();
            return username.equals(userDetails.getUsername()) && !isTokenExpired(claims);
        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired but processed: " + e.getClaims());
            return false; // Token wygasł
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false; // Token jest nieprawidłowy
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            System.out.println("Parsed claims: " + claims);
            return !isTokenExpired(claims); // Sprawdzamy tylko ważność
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        System.out.println("Token expiration date: " + expiration);
        return expiration.before(new Date());
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
