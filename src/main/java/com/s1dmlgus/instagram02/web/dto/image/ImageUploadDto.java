package com.s1dmlgus.instagram02.web.dto.image;

import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Optional;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageUploadDto {

    private String userId;
    @NotBlank(message = "타이틀을 입력해주세요.")
    private String caption;
    private MultipartFile file;


    public Image toEntity(String postImageUrl, User user){
        return Image.builder()
                .caption(caption)
                .postImageUrl(postImageUrl)
                .user(user)
                .build();
    }
}
