package com.s1dmlgus.instagram02.domain.user;

import com.s1dmlgus.instagram02.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;            // 닉네임

    private String password;            // 패스워드

    private String email;               // 이메일

    private String name;                // 이름

    private String bio;                 // 자기소개
    private String website;             // 웹사이트
    private String phone;               // 핸드폰 번호
    private String profileImageUrl;     // 프로필 사진

    @Enumerated(EnumType.STRING)
    private Gender gender;              // 성별
    @Enumerated(EnumType.STRING)
    private Role role;                  // 권한


    // 비밀번호 암호화
    public void bcryptPw(String encode) {
        this.password = encode;
    }
}
