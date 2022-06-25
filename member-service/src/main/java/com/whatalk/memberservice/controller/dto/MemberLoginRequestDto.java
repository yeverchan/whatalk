package com.whatalk.memberservice.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MemberLoginRequestDto {

    @NotNull
    private String email;

    @NotNull
    private String password;

}
