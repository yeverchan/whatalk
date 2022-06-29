package com.whatalk.memberservice.auth.exception;

import javax.servlet.http.HttpServletResponse;

public enum AuthStatus {

    BAD_CREDENTIALS(HttpServletResponse.SC_UNAUTHORIZED, "사용자 정보가 존재하지 않거나 비밀번호가 틀립니다."),
    NOT_SUPPORTED_METHOD(HttpServletResponse.SC_UNAUTHORIZED, "지원하지 않는 요청입니다."),
    BAD_REQUEST(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");

    private final int value;
    private final String message;

    AuthStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
