package com.whatalk.memberservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class MemberUpdateDto {

    @NotNull(message = "이름이 존재하지 않습니다.")
    @Size(min = 2, max = 10, message = "이름은 2자에서 10자 사이여야 합니다.")
    private String name;

    @Size(max = 50, message = "상태 메시지는 50자 이하여야 합니다.")
    private String status;
}
