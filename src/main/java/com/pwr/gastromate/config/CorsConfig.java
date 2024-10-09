package com.pwr.gastromate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        System.out.println("Setting up CORS filter...");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Ustawia domenę frontendową, która ma dostęp (możesz użyć * dla wszystkich)
        config.setAllowedOrigins(List.of("http://localhost:3000"));

        // Ustawia metody HTTP, które są dozwolone (GET, POST, itd.)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Ustawia nagłówki, które są dozwolone w żądaniach
        config.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization", "X-CSRF-TOKEN"));

        // Ustawia, czy dozwolone są ciasteczka w żądaniach od klienta
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}