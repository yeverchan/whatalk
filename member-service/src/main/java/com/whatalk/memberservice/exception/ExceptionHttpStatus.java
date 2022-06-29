package com.whatalk.memberservice.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionHttpStatus {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    EMAIL_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");

    private final HttpStatus value;
    private final String message;

    ExceptionHttpStatus(HttpStatus value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value.value();
    }

    public String getMessage() {
        return message;
    }
}
