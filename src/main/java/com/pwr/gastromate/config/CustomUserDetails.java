package com.pwr.gastromate.config;

import com.pwr.gastromate.data.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user; // Encja użytkownika z bazy danych

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapowanie ról użytkownika na obiekty GrantedAuthority
        return user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Hasło z encji użytkownika
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // E-mail jako login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Konto zawsze aktywne
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Konto nie jest zablokowane
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Dane logowania zawsze ważne
    }

    @Override
    public boolean isEnabled() {
        return true; // Konto zawsze aktywne
    }
}

