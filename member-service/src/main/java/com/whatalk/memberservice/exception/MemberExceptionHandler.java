package com.whatalk.memberservice.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberApiException.class)
    public ResponseEntity<ErrorResponse> handleMemberApiException(MemberApiException e) {

        return ResponseEntity.status(e.getExceptionHttpStatus().getValue())
                .body(
                        ErrorResponse.builder()
                                .code(e.getExceptionHttpStatus().getValue())
                                .messages(
                                        List.of(
                                                e.getExceptionHttpStatus().getMessage()
                                        )
                                )
                                .build()
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .messages(
                                e.getConstraintViolations().stream()
                                        .map(ConstraintViolation::getMessage)
                                        .collect(Collectors.toList())
                        )
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .messages(
                                e.getAllErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .collect(Collectors.toList())
                        )
                        .build()
                );
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(JsonParseException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .messages(
                                List.of("잘못된 요청입니다.")
                        )
                        .build()
                );
    }

}
