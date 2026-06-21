package com.trainshier.filter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @param global exception handler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param exception runtime exception
     * @return error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException exception) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now());

        response.put(
                "message",
                exception.getMessage());

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value());

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    /**
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception exception) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now());

        response.put(
                "message",
                exception.getMessage());

        response.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}