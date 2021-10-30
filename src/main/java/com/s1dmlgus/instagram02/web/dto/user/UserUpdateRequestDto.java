package com.s1dmlgus.instagram02.web.dto.user;


import com.s1dmlgus.instagram02.domain.user.Gender;
import com.s1dmlgus.instagram02.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequestDto{

    @NotBlank
    @Size(max = 20, message = "0에서 20자까지 작성할 수 있습니다")
    private String name;

    private String website;
    private String bio;
    private String phone;
    private String gender;


}
