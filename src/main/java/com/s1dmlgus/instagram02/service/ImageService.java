package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.image.ImageRepository;

import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class ImageService {

    Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;


    @Value("${file.path}")
    private String uploadFolder;


    // 파일 업로드
    @Transactional
    public ResponseDto<?> upload(ImageUploadDto imageUploadDto) {

        // 영속화
        Image afterUploadImage = imageRepository.save(imageUploadDto.toEntity(createFile(imageUploadDto), getUser(imageUploadDto)));

        logger.info("[after 영속화] : {}", afterUploadImage);

        return new ResponseDto<>("이미지가 업로드 되었습니다.", afterUploadImage.getId());
    }

    // 파일 생성
    protected String createFile(ImageUploadDto imageUploadDto) {
        
        // 파일 유효성검사
        validationFile(imageUploadDto);

        // 파일명
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + imageUploadDto.getFiles().getOriginalFilename();
        // 파일경로
        Path imageFilePath = Paths.get(uploadFolder + fileName);
        // 파일업로드
        uploadFile(imageUploadDto, imageFilePath);

        return fileName;
    }

    // 유효성 검사
    protected void validationFile(ImageUploadDto imageUploadDto) {
        if (imageUploadDto.getFiles() == null) {
            throw new CustomApiException("이미지가 첨부되지 않았습니다.");
        }
    }

    // 유저 get
    protected User getUser(ImageUploadDto imageUploadDto) {

        Optional<User> OptionalUser = userRepository.findById(Long.parseLong(imageUploadDto.getUserId()));

        if (OptionalUser.isEmpty()) {
            throw new CustomApiException("잘못된 요청입니다. 유저가 없습니다.");
        }else
            return OptionalUser.get();
    }

    // 파일 업로드
    protected void uploadFile(ImageUploadDto imageUploadDto, Path imageFilePath) {
        try {
            Files.write(imageFilePath, imageUploadDto.getFiles().getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
