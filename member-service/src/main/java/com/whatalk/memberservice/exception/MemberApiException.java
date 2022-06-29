package com.whatalk.memberservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberApiException extends RuntimeException {

    private final ExceptionHttpStatus exceptionHttpStatus;

}
