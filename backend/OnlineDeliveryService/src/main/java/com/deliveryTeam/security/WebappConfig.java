package com.deliveryTeam.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableMethodSecurity
public class WebappConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(Authorize ->
                                //관리자+점주
                        Authorize.requestMatchers(
                                        HttpMethod.POST, "/api/products/create",
                                        "/api/admin/**"
                                ).hasAnyRole("ADMIN", "RESTAURANT_OWNER")

                                //관리자
                                .requestMatchers(
                                        HttpMethod.POST, "/api/categories",
                                        "/api/users/admin/**"
                                ).hasRole("ADMIN")

                                //로그인 유저만
                                .requestMatchers(
                                        "/api/users/me", "/api/users/me/**",
                                        "/api/cart/**",
                                        "/api/auth/logout",
                                        "/api/auth/me",
                                        "/api/auth/change-password"
                                ).authenticated()

                                //전체 허용(비로그인 포함)
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/api/products/**",
                                        "/api/categories/**"
                                ).permitAll()

                                .anyRequest().permitAll()
                )

                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

                return http.build();
     }

    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                corsConfig.setAllowedMethods(Collections.singletonList("*"));
                corsConfig.setAllowCredentials(true);
                corsConfig.setAllowedHeaders(Collections.singletonList("*"));
                corsConfig.setExposedHeaders(List.of("Authorization"));
                corsConfig.setMaxAge(3600L); // 1 hour
                return corsConfig;
            }
        };
    }
}
