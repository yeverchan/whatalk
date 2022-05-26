package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public List<Member> findAllByName(String name) {
        return memberRepository.findAllByName(name);
    }

    @Override
    public Member create(Member member) {
        checkDuplicateEmail(member);
        return memberRepository.save(member);
    }

    private void checkDuplicateEmail(Member member) {
        findByEmail(member.getEmail()).ifPresent(m -> {
            throw new IllegalStateException();
        });
    }
}
