package com.whatalk.gatewayservice.exception;

public interface ErrorMessage {

    String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    String INVALID_FORMAT_TOKEN = "잘못된 토큰 형식입니다.";
    String NOT_EXIST_AUTH_HEADER = "토큰이 존재하지 않습니다.";
}
