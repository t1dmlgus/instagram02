package com.s1dmlgus.instagram02.web.dto.image.upload;

import com.s1dmlgus.instagram02.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageUploadResponseDto {

    private Long imageId;
    private String caption;
    private String postImageUrl;
    private String username;
    private LocalDateTime createdDate;


    @Builder
    public ImageUploadResponseDto(Image afterUploadImage) {

        this.imageId = afterUploadImage.getId();
        this.createdDate = afterUploadImage.getCreatedDate();
        this.caption = afterUploadImage.getCaption();
        this.postImageUrl = afterUploadImage.getPostImageUrl();
        this.username = afterUploadImage.getUser().getUsername();
    }


}
