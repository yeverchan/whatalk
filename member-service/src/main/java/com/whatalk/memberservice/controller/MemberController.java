package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.controller.dto.MemberCreateRequestDTO;
import com.whatalk.memberservice.controller.dto.MemberResponseDTO;
import com.whatalk.memberservice.controller.dto.MembersDTO;
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
    public ResponseEntity<ResultResponse> createMember(@RequestBody @Valid MemberCreateRequestDTO memberCreateRequestDTO) {

        memberService.create(memberCreateRequestDTO.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResultResponse.builder()
                        .status(ResultStatus.SUCCESS)
                        .build()
        );
    }

    @GetMapping("/{name}")
    public MembersDTO getMembersByName(@PathVariable String name) {

        return MembersDTO.builder().members(memberService.findAllByName(name).stream()
                        .map(member -> MemberResponseDTO.builder()
                                .id(member.getId())
                                .name(member.getEmail())
                                .status(member.getStatus())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
