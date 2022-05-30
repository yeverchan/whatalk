package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Optional<Member> findByEmail(String email);
    List<Member> findAllByName(String name);

    Member create(Member member);

    void changeName(String name, Member member);
    void changeStatus(String status, Member member);
}
