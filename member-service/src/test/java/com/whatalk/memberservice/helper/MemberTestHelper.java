package com.whatalk.memberservice.helper;

import com.whatalk.memberservice.domain.Member;

import com.whatalk.memberservice.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MemberTestHelper {

    private final MemberRepository repository;

    public void createMember(String emailId, String memberName) {
        Member member = Member.builder()
                .email(emailId + "@email.com")
                .password("password")
                .name(memberName)
                .build();

        repository.save(member);
    }

    public void createMembers(int size) {
        for (int i = 1; i <= size; i++) {
            Member member = Member.builder()
                    .email("member" + i + "@email.com")
                    .password("password")
                    .name("member" + i)
                    .build();

            repository.save(member);
        }
    }
}
