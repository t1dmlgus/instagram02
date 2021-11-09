package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.image.ImageRepository;

import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RequiredArgsConstructor
@Service
public class ImageService {

    Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    @Value("${file.path}")
    private String uploadFolder;


    // 파일 업로드
    public ResponseDto<?> upload(String caption, MultipartFile file, PrincipalDetails principalDetails) {

        // 파일명
        String filename = Image.createFilename(file);
        // 파일경로
        Path imageFilePath = Paths.get(uploadFolder + filename);

        try {
            Files.write(imageFilePath, file.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 영속화
        Image uploadImage =  Image.builder()
                .caption(caption)
                .postImageUrl(filename)
                .user(principalDetails.getUser())
                .build();

        Image saveImage = imageRepository.save(uploadImage);

        logger.info("upload ImageEntity {}", saveImage);

        return new ResponseDto<>("이미지가 업로드 되었습니다.", saveImage);
    }
}
