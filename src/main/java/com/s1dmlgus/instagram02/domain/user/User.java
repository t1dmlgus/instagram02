package com.s1dmlgus.instagram02.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.s1dmlgus.instagram02.domain.BaseTimeEntity;
import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@ToString(exclude = "images")
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


    @OneToMany(mappedBy = "user")
    private List<Image> images = new ArrayList<Image>();

    @Builder
    public User(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

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

        if (updateRequestDto.getGender().equals("남") || updateRequestDto.getGender().equals("")) {
            this.gender = Gender.MAN;
        }else if(updateRequestDto.getGender().equals("여")){
            this.gender = Gender.WOMAN;
        }

        //this.gender = updateRequestDto.getGender().equals("MAN") ? Gender.MAN : Gender.WOMAN;
    }

}
