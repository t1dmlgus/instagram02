package com.s1dmlgus.instagram02.web.dto.image;

import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.user.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadDto {

    private MultipartFile file;
    private String caption;


    public Image toEntity(String postImageUrl, User user){
        return Image.builder()
                .caption(caption)
                .postImageUrl(postImageUrl)
                .user(user)
                .build();
    }
}
