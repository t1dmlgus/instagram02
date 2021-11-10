package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.image.ImageRepository;

import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class ImageService {

    Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    @Value("${file.path}")
    private String uploadFolder;


    // 파일 업로드
    @Transactional
    public ResponseDto<?> upload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {

        // 파일명
        String filename = createFilename(imageUploadDto.getFile());
        // 파일경로
        Path imageFilePath = Paths.get(uploadFolder + filename);

        try {
            Files.write(imageFilePath, imageUploadDto.getFile().getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 영속화

        Image beforeUploadImage = imageUploadDto.toEntity(filename, principalDetails.getUser());
        Image afterUploadImage = imageRepository.save(beforeUploadImage);


        return new ResponseDto<>("이미지가 업로드 되었습니다.", afterUploadImage);
    }

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


}
