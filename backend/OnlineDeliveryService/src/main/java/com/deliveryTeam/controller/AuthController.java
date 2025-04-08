package com.deliveryTeam.controller;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.deliveryTeam.entity.USER_ROLE;
import com.deliveryTeam.entity.User;
import com.deliveryTeam.http.request.ChangePasswordRequest;
import com.deliveryTeam.http.request.LoginRequest;
import com.deliveryTeam.http.response.Response;
import com.deliveryTeam.repository.CartRepository;
import com.deliveryTeam.repository.UserRepository;
import com.deliveryTeam.security.JwtProvider;
import com.deliveryTeam.service.UserService;
import com.deliveryTeam.service.auth.CustomUserDetailsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 인증 관련 API를 처리하는 컨트롤러 /api/auth/** : 비로그인 사용자도 접근 가능한 인증 관련 API /api/user/** : 로그인한 사용자만 접근 가능한
 * 사용자 관련 API
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customerUserDetailsService;

    /** 회원가입 API - 누구나 접근 가능 */
    @PostMapping("/api/auth/register")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody User user) throws Exception {
        try {
            // 이메일 중복 확인
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new Exception("이미 사용 중인 이메일입니다.");
            }

            // UserService를 통해 사용자 등록
            User savedUser = userService.registerUser(user);

            // 저장된 사용자 정보로 인증 처리
            UserDetails userDetails =
                    customerUserDetailsService.loadUserByUsername(savedUser.getEmail());
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // JWT 토큰 생성
            String token = jwtProvider.generateToken(authentication);

            // 응답 생성
            Response authResponse = new Response();
            authResponse.setJwt(token);
            authResponse.setMessage("회원가입이 완료되었습니다.");
            authResponse.setRole(savedUser.getRole());

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Failed to register user: " + e.getMessage());
        }
    }

    /** 로그인 API - 누구나 접근 가능 */
    @PostMapping("/api/auth/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest req) {
        String username = req.getEmail();
        String password = req.getPassword();

        Authentication authentication = authenticate(username, password);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        Response authResponse = new Response();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login successful");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    /** 이메일 중복 확인 API - 누구나 접근 가능 */
    @GetMapping("/api/auth/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userRepository.existsByEmail(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 확인 중 오류가 발생했습니다.");
        }
    }

    /** 로그아웃 API - 로그인 필요 */
    @PostMapping("/api/user/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("로그아웃되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("로그아웃 중 오류가 발생했습니다.");
        }
    }

    /** 비밀번호 변경 API - 로그인 필요 */
    @PostMapping("/api/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            userService.changePassword(
                    user.getUserId(), request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호 변경 실패: " + e.getMessage());
        }
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

    //  @GetMapping("/api/user/me")
    //  public ResponseEntity<?> getCurrentUser(Authentication authentication) {
    //      if (authentication == null || !authentication.isAuthenticated()) {
    //          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    //      }

    //      String email = authentication.getName();
    //      User user = userService.getUserByEmail(email);

    //      return ResponseEntity.ok(user);
    //  }
}
