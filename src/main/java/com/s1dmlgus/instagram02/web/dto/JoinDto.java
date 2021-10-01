package com.s1dmlgus.instagram02.web.dto;


import com.s1dmlgus.instagram02.domain.user.User;
import lombok.Data;

@Data
public class JoinDto {

    private String username;
    private String password;
    private String email;
    private String name;


    public User toEntity(){

        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .build();
    }
}
