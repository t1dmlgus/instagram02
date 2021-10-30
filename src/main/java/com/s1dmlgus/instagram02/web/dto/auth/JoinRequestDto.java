package com.s1dmlgus.instagram02.web.dto.auth;


import com.s1dmlgus.instagram02.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinRequestDto {

    @NotBlank
    @Size(max = 20, message = "0에서 20자까지 작성할 수 있습니다")
    private String username;

    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    @Size(max = 20, message = "0에서 20자까지 작성할 수 있습니다")
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
