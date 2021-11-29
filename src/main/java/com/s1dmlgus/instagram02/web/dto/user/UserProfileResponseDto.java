package com.s1dmlgus.instagram02.web.dto.user;


import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.user.User;
import lombok.Data;
import java.util.List;

@Data
public class UserProfileResponseDto {

    private Long userId;
    private String username;
    private String bio;
    private String website;
    private List<Image> images;
    private int imageCount;
    private boolean pageOwnerState;


    public UserProfileResponseDto(User user, Long sessionId) {
        this.userId = user.getId();
        this.username = user.getName();
        this.bio = user.getBio();
        this.website = user.getWebsite();
        this.images = user.getImages();
        this.imageCount = user.getImages().size();

        if (userId.equals(sessionId)) {
            pageOwnerState = true;
        }


    }

}
