package com.whatalk.memberservice.repository;

import com.whatalk.memberservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    List<Member> findAllByName(String name);

    boolean existsMemberByEmail(String email);
}
