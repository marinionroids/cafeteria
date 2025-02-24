package com.marin.cafeteria.config;

import com.marin.cafeteria.config.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtFilter jwtFilter;
    public SecurityConfig(final JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Server endpoints
                        .requestMatchers("/api/products/**").hasAnyAuthority("SERVER", "MANAGER")
                        .requestMatchers("/api/order/**").hasAnyAuthority("SERVER", "MANAGER")
                        .requestMatchers("/api/past-orders/**").hasAnyAuthority("SERVER", "MANAGER")
                        .requestMatchers("/api/receipt/**").hasAnyAuthority("SERVER", "MANAGER")
                        .requestMatchers("/api/reports/**").hasAnyAuthority("SERVER", "MANAGER")




                        // Manager endpoints
                        .requestMatchers("/api/staff/**").hasAuthority("MANAGER")
                        .requestMatchers("/api/register/**").hasAuthority("MANAGER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors
                        .configure(http));
        return http.build();
    }

}
