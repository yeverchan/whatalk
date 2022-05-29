package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.service.MemberDTO;
import com.whatalk.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{name}")
    public List<MemberDTO> findMembersByName(@PathVariable String name) {
        // TODO: 2022/05/28 해당 name를 가진 멤버가 존재하지 않을 수 도 있기 때문에 200 반환하기.
        return memberService.findAllByName(name);
    }
}
