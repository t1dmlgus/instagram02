package com.s1dmlgus.instagram02.web.dto.image.story;

import com.s1dmlgus.instagram02.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageStoryResponseDto {

    private Long imageId;
    private String caption;
    private String postImageUrl;
    private String username;
    private LocalDateTime createdDate;
    private int likeCount;
    private boolean likeState;


    public ImageStoryResponseDto(Image uploadImage, int likeCount) {

        this.imageId = uploadImage.getId();
        this.createdDate = uploadImage.getCreatedDate();
        this.caption = uploadImage.getCaption();
        this.postImageUrl = uploadImage.getPostImageUrl();
        this.username = uploadImage.getUser().getUsername();


    }

    // story
    public static List<ImageStoryResponseDto> imageStoryResponseDtoList(List<Image> storyImage, int likeCount) {

        List<ImageStoryResponseDto> ar = new ArrayList<>();
        for (Image image : storyImage) {
            ar.add(new ImageStoryResponseDto(image, likeCount));
        }

        return ar;
    }


}
