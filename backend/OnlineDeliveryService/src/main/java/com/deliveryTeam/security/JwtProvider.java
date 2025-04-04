package com.deliveryTeam.security;

import java.util.*;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtConstant.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication auth) {
        // Authentication 객체에서 권한 정보를 추출
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // JWT를 생성하여 반환
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + JwtConstant.EXPIRATION_TIME))
                .claim("email", auth.getName()) // 사용자 이메일을 "email" 클레임에 저장
                .claim("authorities", roles) // 권한 정보를 "authorities" 클레임에 저장
                .signWith(getSigningKey()) // 비밀 키로 서명
                .compact(); // JWT를 컴팩트하게 생성하여 반환
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        // Set에 저장된 권한들을 콤마로 구분하여 하나의 문자열로 반환
        return String.join(",", auths);
    }

    public String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7);
        Claims claims =
                Jwts.parser()
                        .verifyWith(this.getSigningKey()) // 서명 검증
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
        // "email" 클레임에서 이메일 정보 추출하여 반환
        return String.valueOf(claims.get("email"));
    }
}
