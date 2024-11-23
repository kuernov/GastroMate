package com.pwr.gastromate.controller;


import com.pwr.gastromate.config.JwtService;
import com.pwr.gastromate.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/registration")
    public void register(@RequestBody @Validated RegistrationRequest request) {
        userService.register(request);
    }

    public record RegistrationRequest(String email, String password, String username, String street, String city, String postalCode
    ) {}

    @PostMapping("/login")
    public LoginResponse logIn(@RequestBody @Validated LoginRequest request) {
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

        return new LoginResponse(accessToken, cookie.toString());

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        if (jwtService.isTokenValid(refreshToken)) {
            String username = jwtService.extractUsername(refreshToken);
            var userDetails = userService.loadUserByUsername(username);

            String newAccessToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
