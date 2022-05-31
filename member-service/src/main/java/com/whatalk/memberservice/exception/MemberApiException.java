package com.whatalk.memberservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MemberApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
}