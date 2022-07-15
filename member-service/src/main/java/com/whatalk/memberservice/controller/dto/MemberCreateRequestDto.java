package com.whatalk.memberservice.controller.dto;

import com.whatalk.memberservice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class MemberCreateRequestDto {

    @NotNull(message = "이메일이 존재하지 않습니다.")
    @Email
    private String email;

    @NotNull(message = "비밀번호가 존재하지 않습니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String password;

    @NotNull(message = "이름이 존재하지 않습니다.")
    @Size(min = 2, max = 10, message = "이름은 2자에서 10자 사이여야 합니다.")
    private String name;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
