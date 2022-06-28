package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.controller.dto.MemberCreateRequestDto;
import com.whatalk.memberservice.controller.dto.MemberResponseDto;
import com.whatalk.memberservice.controller.dto.MembersDto;
import com.whatalk.memberservice.controller.dto.ResponseResult;
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

        return MembersDto.builder().members(memberService.findAllByName(name).stream()
                        .map(member -> MemberResponseDto.builder()
                                .id(member.getId())
                                .name(member.getEmail())
                                .status(member.getStatus())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
