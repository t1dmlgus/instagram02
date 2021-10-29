package com.s1dmlgus.instagram02.web.dto.user;


import com.s1dmlgus.instagram02.domain.user.Gender;
import com.s1dmlgus.instagram02.domain.user.User;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class UserUpdateRequestDto {

    private String name;
    private String website;
    private String bio;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;


}
