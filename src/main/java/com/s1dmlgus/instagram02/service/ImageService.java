package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.image.ImageRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.story.ImageStoryResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.upload.ImageUploadRequestDto;
import com.s1dmlgus.instagram02.web.dto.image.upload.ImageUploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class ImageService {

    Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Value("${file.path}")
    private String uploadFolder;


    // 이미지 업로드
    @Transactional
    public ResponseDto<?> upload(ImageUploadRequestDto imageUploadRequestDto, PrincipalDetails principalDetails){

        // 파일 명
        String fileName = createFile(imageUploadRequestDto);

        // 파일업로드
        //uploadFile(imageUploadDto, fileName);

        // s3 파일 업로드
        s3Service.upload(imageUploadRequestDto, fileName);


        // 영속화
        Image afterUploadImage = imageRepository.save(imageUploadRequestDto.toEntity(fileName, principalDetails.getUser()));
        logger.info("[after 영속화] : {}", afterUploadImage);

        return new ResponseDto<>("이미지가 업로드 되었습니다.", new ImageUploadResponseDto(afterUploadImage));
    }

    // 파일명
    protected String createFile(ImageUploadRequestDto imageUploadRequestDto) {
        
        // 파일 유효성검사
        validationFile(imageUploadRequestDto);
        // 파일명
        UUID uuid = UUID.randomUUID();

        return uuid + "_" + imageUploadRequestDto.getFile().getOriginalFilename();
    }

    // 유효성 검사
    protected void validationFile(ImageUploadRequestDto imageUploadRequestDto) {
        if (imageUploadRequestDto.getFile() == null) {
            throw new CustomApiException("이미지가 첨부되지 않았습니다.");
        }
    }

    // 파일 업로드
    protected void uploadFile(ImageUploadRequestDto imageUploadRequestDto, String fileName) {

        // 파일경로
        Path imageFilePath = Paths.get(uploadFolder + fileName);

        try {
            Files.write(imageFilePath, imageUploadRequestDto.getFile().getBytes());

        } catch (Exception e) {
            throw new CustomApiException("이미지 업로드에 실패했습니다.");

        }
    }

    // 스토리 가져오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getStory(PrincipalDetails principalDetails) {

        List<Image> story = imageRepository.getStory(principalDetails.getUser().getId());

        return new ResponseDto<>("이미지 스토리를 가져옵니다.", ImageStoryResponseDto.imageStoryResponseDtoList(story, 0));

    }
}
