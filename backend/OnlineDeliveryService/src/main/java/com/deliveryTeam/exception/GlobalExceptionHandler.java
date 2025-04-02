package com.deliveryTeam.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage()); // 예외 메시지
        response.put("status", HttpStatus.BAD_REQUEST.value()); // 상태 코드
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase()); // 에러 타입

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
