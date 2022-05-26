package com.whatalk.memberservice.repository;

import com.whatalk.memberservice.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @DisplayName("멤버 세이브 테스트")
    @Test
    void test_save_member() {

        Member member = Member.builder()
                .email("test@email.com")
                .password("password")
                .name("test")
                .build();

        memberRepository.save(member);

        Member result = memberRepository.findByEmail(member.getEmail()).get();
        assertThat(result).isEqualTo(member);
    }

    @DisplayName("모든 멤버 검색 테스트")
    @Test
    void test_findAll_member() {

        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList.size()).isEqualTo(0);

        createMembers(3);

        memberList = memberRepository.findAll();
        assertThat(memberList.size()).isEqualTo(3);
    }

    @DisplayName("같은 이름의 멤버 모두 검색")
    @Test
    void test_findAllByName() {

        createMember("테스트1", "테스트");
        createMember("테스트2", "테스트");
        createMember("테스트3", "테스트");
        createMember("멤버", "멤버");
        createMember("유저", "유저");

        List<Member> equalsNameMemberList = memberRepository.findAllByName("테스트");
        assertThat(equalsNameMemberList.size()).isEqualTo(3);
    }


    public void createMember(String emailId, String memberName) {
        Member member = Member.builder()
                .email(emailId + "@email.com")
                .password("password")
                .name(memberName)
                .build();

        memberRepository.save(member);
    }

    public void createMembers(int size) {
        for (int i = 1; i <= size; i++) {
            Member member = Member.builder()
                    .email("member" + i + "@email.com")
                    .password("password")
                    .name("member" + i)
                    .build();

            memberRepository.save(member);
        }
    }
}