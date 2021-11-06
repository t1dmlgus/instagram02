package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.image.ImageRepository;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${file.path}")
    private String uploadFolder;


    public void upload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {

        // 파일명
        String filename = Image.createFilename(imageUploadDto);
        // 파일경로
        Path imageFilePath = Paths.get(uploadFolder + filename);

        try {
            Files.write(imageFilePath, imageUploadDto.getFile().getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 영속화
        Image uploadImage = imageUploadDto.toEntity(filename, principalDetails.getUser());
        imageRepository.save(uploadImage);
    }
}
