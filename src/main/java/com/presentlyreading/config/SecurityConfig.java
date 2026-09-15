package com.presentlyreading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration for the Presently Reading API.
 *
 * <p>Strategy:
 * <ul>
 *   <li>All GET requests to any path — public.</li>
 *   <li>POST /api/newsletter/** and POST /api/contact — public (fan-facing).</li>
 *   <li>All other POST/PUT/DELETE — protected with HTTP Basic (admin).</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) // delegated to CorsConfig
            .authorizeHttpRequests(auth -> auth
                // All GET requests are public (covers /api/songs/1/lyrics etc.)
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/**")).permitAll()
                // Fan-facing POST endpoints are public
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/newsletter/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/contact")).permitAll()
                // Everything else (admin writes) requires Basic auth
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});
        return http.build();
    }
}
