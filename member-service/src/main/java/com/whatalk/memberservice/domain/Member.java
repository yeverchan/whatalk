package com.whatalk.memberservice.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String password;

    @NotNull
    @Size(min = 2, max = 10, message = "이름은 2자에서 10자 사이여야 합니다.")
    private String name;

    @Size(max = 50, message = "상태 메시지는 50자 이하여야 합니다.")
    private String status;

//    private String profileImageUrl;

    public void changeName(String name){
        this.name = name;
    }

    public void changeStatus(String status){
        this.status = status;
    }
}
