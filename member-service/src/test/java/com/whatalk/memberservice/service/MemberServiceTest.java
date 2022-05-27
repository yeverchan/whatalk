package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;


    @DisplayName("회원 가입 테스트")
    @Test
    void test_create(){
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
    void test_checkDuplicateEmail(){
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
        Assertions.assertThrows(IllegalStateException.class, () -> memberService.create(member2));
    }

    @DisplayName("이름 변경 테스트")
    @Test
    void test_changeName(){
        Member member = Member.builder()
                .email("테스트@email.com")
                .password("password")
                .name("멤버")
                .build();

        memberService.create(member);

        memberService.changeName("변경할 이름", member);

        assertThat(member.getName()).isEqualTo("변경할 이름");

        //        List<String> name = memberService.findAllByName("변경할 이름").stream().map(Member::getName).collect(Collectors.toList());
    }

    @DisplayName("상태 메시지 변경 테스트")
    @Test
    void test_changeStatus(){
        Member member = Member.builder()
                .email("테스트@email.com")
                .password("password")
                .name("멤버")
                .build();

        memberService.create(member);

        Assertions.assertNull(member.getStatus());

        memberService.changeStatus("안녕하세요", member);

        assertThat(member.getStatus()).isEqualTo("안녕하세요");

    }
}
