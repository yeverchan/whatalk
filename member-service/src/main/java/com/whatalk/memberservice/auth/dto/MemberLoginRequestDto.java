package com.whatalk.memberservice.auth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MemberLoginRequestDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String password;

}
