package com.deliveryTeam.security;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtConstant.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt =
                request.getHeader(
                        JwtConstant
                                .JWT_HEADER); // request.getHeader("Authorization") => get jwt from
        // header

        // Bearer Token
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // remove "Bearer " from jwt
            System.out.println("JWT Token: " + jwt);
            try {
                // JWT를 파싱하고 서명 키로 검증하여 Claims 객체로 변환
                Claims claims =
                        Jwts.parser()
                                .verifyWith(this.getSigningKey())
                                .build()
                                .parseSignedClaims(jwt)
                                .getPayload();
                // JWT에서 이메일 및 권한 정보 가져오기
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auth =
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                // Authentication 객체 생성
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                email, null, auth); // null -> 비밀번호는 JWT 인증에서는 필요하지 않음
                // authenticated user set in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new BadCredentialsException("invalid token.......");
            }
        }
        filterChain.doFilter(request, response);
    }
}
