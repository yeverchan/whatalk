package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.exception.MemberApiException;
import com.whatalk.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        checkDuplicateEmail(member.getEmail());
        return memberRepository.save(member);
    }

    @Override
    public void changeName(String name, Long id) {
        Member target = getMember(id);

        target.changeName(name);
//      Dirty Checking,  memberRepository.save(target);
    }

    @Override
    public void changeStatus(String status, Long id) {
        Member target = getMember(id);

        target.changeStatus(status);
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(
                        () -> new MemberApiException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다.")
                );
    }

    private void checkDuplicateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new MemberApiException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
        });
    }
}
