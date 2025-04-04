package com.deliveryTeam.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.deliveryTeam.entity.User;
import com.deliveryTeam.http.request.UpdateUserRequest;
import com.deliveryTeam.service.UserService;

import lombok.RequiredArgsConstructor;

/** 사용자 관련 기능을 처리하는 컨트롤러 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 현재 인증된 사용자 정보 조회 */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /** 현재 인증된 사용자 정보 수정 */
    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@RequestBody UpdateUserRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(request.getPassword());
            }

            User updatedUser = userService.updateUser(user.getUserId(), user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /** 현재 인증된 사용자 삭제 */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            userService.deleteUser(user.getUserId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

/** 관리자용 API를 처리하는 컨트롤러 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
class AdminController {

    private final UserService userService;

    /** 모든 사용자 조회 */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** 특정 사용자 조회 */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** 이메일로 사용자 조회 */
    @GetMapping("/users/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
