package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.service.MemberDTO;
import com.whatalk.memberservice.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    MemberService memberService;

    @Autowired
    WebTestClient client = WebTestClient.bindToController(new MemberController(memberService)).build();


    @BeforeEach
    void initMembers() {
        Member member1 = Member.builder()
                .email("테스트1@email.com")
                .password("password")
                .name("멤버")
                .build();
        Member member2 = Member.builder()
                .email("테스트2@email.com")
                .password("password")
                .name("멤버")
                .build();
        Member member3 = Member.builder()
                .email("테스트3@email.com")
                .password("password")
                .name("멤버")
                .build();
        Member member4 = Member.builder()
                .email("테스트4@email.com")
                .password("password")
                .name("유저")
                .build();

        memberService.create(member1);
        memberService.create(member2);
        memberService.create(member3);
        memberService.create(member4);
    }


    @DisplayName("같은 이름 조회 api 테스트")
    @Test
    void test_findMembersByName() {

        List<MemberDTO> result = client.get().uri("/members/멤버")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MemberDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(result.size()).isEqualTo(3);

    }
}