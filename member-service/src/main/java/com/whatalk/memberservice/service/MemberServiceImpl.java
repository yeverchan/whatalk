package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

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

    @Override
    public void changeName(String name, Member member) {
        Member target = getMember(member);

        target.setName(name);
//      Dirty Checking,  memberRepository.save(target);
    }

    @Override
    public void changeStatus(String status, Member member){
        Member target = getMember(member);

        target.setStatus(status);
    }

    private Member getMember(Member member) {
        return memberRepository.findById(member.getId())
                .orElseThrow(
                        () -> new IllegalStateException("사용자 정보를 찾을 수 없습니다.")
                );
    }

    private void checkDuplicateEmail(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });
    }
}
