package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.controller.dto.MemberCreateRequestDTO;
import com.whatalk.memberservice.controller.dto.MemberResponseDTO;
import com.whatalk.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberCreateRequestDTO memberCreateRequestDTO) {

        memberService.create(memberCreateRequestDTO.toEntity());
        // TODO: 2022/05/30 Result Status 클래스를 만들어 응답하기
        return ResponseEntity.ok().build();
    }

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
