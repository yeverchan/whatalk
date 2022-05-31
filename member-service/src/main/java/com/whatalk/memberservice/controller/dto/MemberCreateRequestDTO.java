package com.whatalk.memberservice.controller.dto;

import com.whatalk.memberservice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberCreateRequestDTO {
    private String email;
    private String password;
    private String name;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}