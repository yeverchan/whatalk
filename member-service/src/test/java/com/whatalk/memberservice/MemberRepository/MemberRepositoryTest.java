package com.whatalk.memberservice.MemberRepository;

import com.whatalk.memberservice.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;

    @DisplayName("멤버 세이브 테스트")
    @Test
    void test_save_member(){
        Member member = Member.builder()
                .email("test@email.com")
                .password("password")
                .name("test")
                .build();

        repository.save(member);

        Member result = repository.findByEmail(member.getEmail()).get();

        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result).isEqualTo(member);

    }
}