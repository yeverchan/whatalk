package com.whatalk.memberservice.service;

import com.whatalk.memberservice.auth.exception.AuthStatus;
import com.whatalk.memberservice.domain.Member;
import com.whatalk.memberservice.exception.ExceptionHttpStatus;
import com.whatalk.memberservice.exception.MemberApiException;
import com.whatalk.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new MemberApiException(ExceptionHttpStatus.MEMBER_NOT_FOUND)
                );
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(
                        () -> new MemberApiException(ExceptionHttpStatus.MEMBER_NOT_FOUND)
                );
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

    // TODO: 2022/06/29
    @Override
    public Member changeName(String name, Long id) {
        Member target = findById(id);

        target.changeName(name);

        return target;
    }

    // TODO: 2022/06/29
    @Override
    public Member changeStatus(String status, Long id) {
        Member target = findById(id);

        target.changeStatus(status);

        return target;
    }


    private void checkDuplicateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new MemberApiException(ExceptionHttpStatus.EMAIL_CONFLICT);
        });
    }

    private void checkExistsMember(String email) {
        if(memberRepository.existsMemberByEmail(email)){
            throw new MemberApiException(ExceptionHttpStatus.MEMBER_NOT_FOUND);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(AuthStatus.BAD_CREDENTIALS.getMessage())
                );

        return new User(member.getEmail(), member.getPassword(), new ArrayList<>());
    }
}
