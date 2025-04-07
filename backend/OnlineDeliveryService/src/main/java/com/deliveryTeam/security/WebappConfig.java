package com.deliveryTeam.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class WebappConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(
                        management ->
                                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        Authorize ->
                                Authorize
                                        // ✅ 누구나 접근 가능한 공개 API (비로그인 사용자도 허용)
                                        .requestMatchers(
                                                "/api/auth/**", // 로그인, 회원가입 등
                                                "/api/products/**", // 음식 목록, 음식 상세
                                                "/api/categories/**", // 음식 카테고리
                                                "/predict", "/predict/**" // 👉 까먹지 말고 나중에 지우자 꼭
                                                "api/stores/**" // 매장 목록, 매장 상세

                                                )
                                        .permitAll()
                                        // ✅ 관리자 또는 음식점 점주만 접근 가능한 관리자 API
                                        .requestMatchers("/api/admin/**")
                                        .hasAnyRole("RESTAURANT_OWNER", "ADMIN")
                                        // ✅ 로그인한 일반 사용자(고객)만 접근 가능한 API
                                        .requestMatchers(
                                                "/api/user/**", // 내 프로필 조회/수정
                                                "/api/orders/**", // 주문 생성, 주문 내역
                                                "/api/cart/**", // 장바구니 관리
                                                "/api/payments/**" // 결제
                                                )
                                        .hasRole("CUSTOMER")
                                        // ✅ 그 외 모든 요청은 로그인만 되어 있으면 허용
                                        .anyRequest()
                                        .authenticated())
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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
