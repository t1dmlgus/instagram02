package com.s1dmlgus.instagram02.domain.image;

import com.s1dmlgus.instagram02.domain.BaseTimeEntity;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    public static String createFilename(MultipartFile file) {
        
        // 유효성 검사
        if (file == null) {
            throw new CustomApiException("이미지가 첨부되지 않았습니다.");
        }
        
        // 파일명
        UUID uuid = UUID.randomUUID();
        return uuid+"_"+file.getOriginalFilename();
    }


    // 좋아요
    // 댓글

}
