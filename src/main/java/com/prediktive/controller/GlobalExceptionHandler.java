package com.prediktive.controller;

import com.prediktive.domain.api.MessageResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<MessageResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        MessageResponse messageResponse = MessageResponse.builder().message("Token has expired. Please ask for another question.").build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
    }
}
