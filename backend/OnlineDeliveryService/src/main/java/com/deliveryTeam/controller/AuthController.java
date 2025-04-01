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
import com.deliveryTeam.repository.UserRepository;
import com.deliveryTeam.security.JwtProvider;
import com.deliveryTeam.service.UserService;
import com.deliveryTeam.service.auth.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customerUserDetailsService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws Exception {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인
    //    @PostMapping("/login")
    //    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    //        try {
    //            User user = userService.login(loginRequest.getEmail(),
    // loginRequest.getPassword());
    //            return ResponseEntity.ok(user);
    //        } catch (RuntimeException e) {
    //            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " +
    // e.getMessage());
    //        }
    //    }
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest req) {
        String username = req.getEmail();
        String password = req.getPassword();

        // loadUserByUsername(), UsernamePasswordAuthenticationToken() 를 통해
        // authentication(Principal, Credentials, Authorities)를 얻음
        Authentication authentication = authenticate(username, password);

        // get authorities
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        // generate token
        String jwt = jwtProvider.generateToken(authentication);

        // Response
        Response authResponse = new Response();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login successful");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("로그아웃되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("로그아웃 중 오류가 발생했습니다.");
        }
    }

    // 현재 로그인된 유저 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }
    }

    // 비밀번호 변경
    @PostMapping("/change-password")
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

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userRepository.existsByEmail(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 확인 중 오류가 발생했습니다.");
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
        // Principal (주체), Credentials(자격 증명), Authorities
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}
