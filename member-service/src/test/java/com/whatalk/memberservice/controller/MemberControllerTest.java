package com.whatalk.memberservice.controller;

import com.whatalk.memberservice.controller.dto.MemberCreateRequestDTO;
import com.whatalk.memberservice.controller.dto.MembersDTO;
import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.exception.ErrorResponse;
import com.whatalk.memberservice.repository.MemberRepository;
import com.whatalk.memberservice.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

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

    @AfterEach
    void clearMembers(){
        memberRepository.deleteAll();
    }

    @DisplayName("같은 이름 조회 api 테스트")
    @Test
    void test_getMembersByName() {
        MembersDTO result = client.get().uri("/members/멤버")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MembersDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(result.getMembers().size()).isEqualTo(3);

    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    void test_create_success() {
        ResultResponse response = client.post().uri("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(MemberCreateRequestDTO.builder()
                        .email("회원가입@email.com")
                        .password("password")
                        .name("테스트")
                        .build())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResultResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response.getStatus()).isEqualTo(ResultStatus.SUCCESS);
    }

    @DisplayName("회원가입 실패(중복된 이메일) 테스트")
    @Test
    void test_create_failure() {
        ErrorResponse response = client.post().uri("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(MemberCreateRequestDTO.builder()
                        .email("테스트1@email.com")
                        .password("password")
                        .name("테스트")
                        .build())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");

    }
}