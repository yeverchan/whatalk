package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.exception.MemberApiException;
import org.junit.jupiter.api.Assertions;
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
        MemberApiException exception = assertThrows(MemberApiException.class, () -> memberService.create(member2));
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
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

        memberService.changeName("변경할 이름", member.getId());

        assertThat(member.getName()).isEqualTo("변경할 이름");

    }

    @DisplayName("이름 변경 실패(존재하지 않는 멤버) 테스트")
    @Test
    void test_changeName_failure(){

        RuntimeException exception = assertThrows(
                MemberApiException.class,
                ()-> memberService.changeName("변경할 이름", 9L)
        );

        assertThat(exception.getMessage()).isEqualTo("사용자 정보를 찾을 수 없습니다.");
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

        memberService.changeStatus("안녕하세요", member.getId());

        assertThat(member.getStatus()).isEqualTo("안녕하세요");

    }

    @DisplayName("상태 메시지 변경 실패(존재하지 않는 멤버) 테스트")
    @Test
    void test_changeStatus_failure(){

        RuntimeException exception = assertThrows(
                MemberApiException.class,
                ()-> memberService.changeStatus("안녕하세요", 9L)
        );

        assertThat(exception.getMessage()).isEqualTo("사용자 정보를 찾을 수 없습니다.");
    }

    @DisplayName("같은 이름의 모든 멤버 검색 테스트")
    @Test
    void test_findAllByName(){
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

        List<Member> memberList = memberService.findAllByName("멤버");

        assertThat(memberList.size()).isEqualTo(3);
    }
}
