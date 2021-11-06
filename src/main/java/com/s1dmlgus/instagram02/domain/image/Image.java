package com.s1dmlgus.instagram02.domain.image;

import com.s1dmlgus.instagram02.domain.BaseTimeEntity;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private String postImageUrl;


    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;


    // 파일명 생성
    public static String createFilename(ImageUploadDto imageUploadDto) {

        // 파일명
        UUID uuid = UUID.randomUUID();
        return uuid+"_"+imageUploadDto.getFile().getOriginalFilename();

    }


    // 좋아요
    // 댓글

}
