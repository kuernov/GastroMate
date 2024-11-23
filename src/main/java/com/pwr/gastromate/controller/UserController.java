package com.pwr.gastromate.controller;


import com.pwr.gastromate.config.JwtService;
import com.pwr.gastromate.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public LoginResponse logIn(@RequestBody @Validated LoginRequest request) { return userService.logIn(request); }

    public record LoginRequest(@NotBlank String email, @NotBlank String password) {}

    public record LoginResponse(String token) {}

}
