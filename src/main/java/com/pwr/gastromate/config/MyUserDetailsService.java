package com.pwr.gastromate.config;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Repozytorium użytkowników

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Pobierz użytkownika z bazy danych na podstawie loginu
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Zmapuj użytkownika na obiekt UserDetails
        return new CustomUserDetails(user);
    }
}
