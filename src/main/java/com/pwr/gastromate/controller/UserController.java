package com.pwr.gastromate.controller;


import com.pwr.gastromate.config.JwtService;
import com.pwr.gastromate.config.MyUserDetailsService;
import com.pwr.gastromate.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;


    @PostMapping("/registration")
    public void register(@RequestBody @Validated RegistrationRequest request) {
        userService.register(request);
    }

    public record RegistrationRequest(String email, String password, String username, String street, String city, String postalCode
    ) {}

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody @Validated LoginRequest request) {
        var userDetails = userService.authenticate(request.email(), request.password());

        // Generowanie tokenów
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Ustawienie ciasteczka HttpOnly z refresh token
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Zmienna dla HTTPS (ustaw na true w środowisku produkcyjnym)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 dni
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString()) // Dodanie ciasteczka do nagłówków odpowiedzi
                .body(Map.of(
                        "accessToken", accessToken // Access token zwracany w odpowiedzi
                ));

    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !jwtService.isRefreshTokenValid(refreshToken)) {
            System.out.println("Is refresh token valid: " + jwtService.isRefreshTokenValid(refreshToken));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        try {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String newAccessToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to refresh access token");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // Ustaw na true w produkcji
                .path("/")
                .maxAge(0) // Usunięcie ciasteczka
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out");
    }

    public record LoginRequest(@NotBlank String email, @NotBlank String password) {}

    public record LoginResponse(String accessToken, String refreshTokenCookie) {}



}
