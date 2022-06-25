package com.whatalk.memberservice.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {

    private Long id;
    private String name;
    private String status;
    //    private String profilePictureLink;

}
