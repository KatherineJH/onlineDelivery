package com.deliveryTeam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 세션을 Stateless로 설정 (JWT를 사용할 때 세션 관리가 필요 없으므로)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청에 대한 인증 설정
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/**")
                                        .permitAll() // 모든 요청을 인증 없이 허용
                                        .anyRequest()
                                        .permitAll() // 나머지 모든 요청도 허용
                        )
                // CSRF 비활성화 (REST API에선 일반적으로 사용하지 않음)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
