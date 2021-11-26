package com.s1dmlgus.instagram02.domain.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.s1dmlgus.instagram02.domain.BaseTimeEntity;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;

@Builder
@ToString(exclude = "user")
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



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    //...

    // 좋아요
    // 댓글

}
