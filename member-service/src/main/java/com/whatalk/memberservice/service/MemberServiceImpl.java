package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.exception.MemberApiException;
import com.whatalk.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

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

        return memberRepository.save(
                Member.builder()
                        .email(member.getEmail())
                        .password(passwordEncoder.encode(member.getPassword()))
                        .name(member.getName())
                        .build()
        );
    }

    @Override
    public void changeName(String name, Long id) {
        Member target = getMember(id);

        target.changeName(name);
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("사용자 정보가 존재하지 않거나 비밀번호가 틀립니다.")
                );

        return new User(member.getEmail(), member.getPassword(), new ArrayList<>());
    }
}
