package com.s1dmlgus.instagram02.web.dto.user.update;


import com.s1dmlgus.instagram02.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateResponseDto {

    private Long id;
    private String username;
    private String name;
    private String bio;
    private String website;
    private String email;
    private String phone;
    private String profileImageUrl;


    public UserUpdateResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.bio = user.getBio();
        this.website = user.getWebsite();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.profileImageUrl = user.getProfileImageUrl();

    }
}
