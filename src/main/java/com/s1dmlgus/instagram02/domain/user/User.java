package com.s1dmlgus.instagram02.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.s1dmlgus.instagram02.domain.BaseTimeEntity;
import com.s1dmlgus.instagram02.web.dto.auth.JoinResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;            // 닉네임

    @JsonIgnore
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
    // 권한 설정
    public void setRole(Role role) {
        this.role = role;
    }


    // 프로필 업데이트
    public void updateUserProfile(UserUpdateRequestDto updateRequestDto) {

        this.name = updateRequestDto.getName();
        this.website = updateRequestDto.getWebsite();
        this.phone = updateRequestDto.getPhone();
        this.bio = updateRequestDto.getBio();
    }

}
