package com.whatalk.memberservice.service;

import com.whatalk.memberservice.domain.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {

    Member getMemberByEmail(String email);
    Member getMemberById(Long id);
    List<Member> getAllMemberByName(String name);

    Member create(Member member);

    Member modifyMemberInfo(String email, String name, String status);
}
