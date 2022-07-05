package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.exception.MemberApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;


    @DisplayName("회원 가입 테스트")
    @Test
    void test_create() {
        Member member = Member.builder()
                .email("테스트@email.com")
                .password("password")
                .name("멤버")
                .build();

        Member result = memberService.create(member);

        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("중복 회원 예외 테스트")
    @Test
    void test_checkDuplicateEmail() {
        Member member1 = Member.builder()
                .email("테스트@email.com")
                .password("password")
                .name("멤버")
                .build();

        Member member2 = Member.builder()
                .email("테스트@email.com")
                .password("password")
                .name("멤버")
                .build();

        memberService.create(member1);

        MemberApiException exception = assertThrows(
                MemberApiException.class, () -> memberService.create(member2)
        );

        assertThat(exception.getExceptionHttpStatus().getValue()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("같은 이름의 모든 멤버 검색 테스트")
    @Test
    void test_findAllByName() {
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

        List<Member> memberList = memberService.getAllMemberByName("멤버");

        assertThat(memberList.size()).isEqualTo(3);
    }

    @DisplayName("멤버 정보 변경 테스트")
    @Test
    void test_modifyMember() {
        Member member = Member.builder()
                .email("테스트@email.com")
                .password("password")
                .name("멤버")
                .build();

        memberService.create(member);

        memberService.modifyMemberInfo(member.getEmail(), "변경할 이름", "변경할 상태 메시지");

        Member modified = memberService.getMemberByEmail(member.getEmail());

        assertThat(modified.getName()).isEqualTo("변경할 이름");
        assertThat(modified.getStatus()).isEqualTo("변경할 상태 메시지");
    }
}
