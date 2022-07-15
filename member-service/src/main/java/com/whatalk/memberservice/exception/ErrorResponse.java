package com.whatalk.memberservice.exception;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private List<String> messages;
}
