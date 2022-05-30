package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.controller.dto.MemberResponseDTO;
import com.whatalk.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // TODO: 2022/05/29 회원가입시 중복된 이메일 중복 여부를 알려주고 409 반환

    @GetMapping("/members/{name}")
    public List<MemberResponseDTO> findMembersByName(@PathVariable String name) {

        return memberService.findAllByName(name).stream()
                .map(member -> MemberResponseDTO.builder()
                        .id(member.getId())
                        .name(member.getEmail())
                        .status(member.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
