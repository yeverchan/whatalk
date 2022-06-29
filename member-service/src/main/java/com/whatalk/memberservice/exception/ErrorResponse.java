package com.whatalk.memberservice.exception;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String message;
}
