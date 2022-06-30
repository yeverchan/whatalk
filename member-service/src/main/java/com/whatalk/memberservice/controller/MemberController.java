package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.controller.dto.*;
import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public MemberResponseDto getMemberByEmail(@RequestHeader(name = "email") String email){

        Member member = memberService.getMemberByEmail(email);

        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .status(member.getStatus())
                .build();
    }

    @PostMapping
    public ResponseEntity<ResponseResult> createMember(@RequestBody @Valid MemberCreateRequestDto memberCreateRequestDTO) {

        memberService.create(memberCreateRequestDTO.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseResult.builder()
                        .success(true)
                        .build()
        );
    }

    @GetMapping("/{name}")
    public MembersDto getMembersByName(@PathVariable String name) {

        return MembersDto.builder().members(memberService.getAllMemberByName(name).stream()
                        .map(member -> MemberResponseDto.builder()
                                .id(member.getId())
                                .name(member.getName())
                                .status(member.getStatus())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @PatchMapping("/info")
    public MemberDto modifyMemberInfo(@RequestHeader String email, @RequestBody MemberUpdateDto memberUpdateDto){

        Member member = memberService.modifyMemberInfo(email, memberUpdateDto.getName(), memberUpdateDto.getStatus());

        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .status(member.getStatus())
                .build();
    }
}
