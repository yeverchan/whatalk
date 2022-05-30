package com.whatalk.memberservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(MemberApiException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                                .message(e.getMessage())
                                .build()
                );
    }
}
