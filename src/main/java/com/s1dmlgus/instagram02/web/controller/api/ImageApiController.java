package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.ImageService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageApiController {

    Logger logger = LoggerFactory.getLogger(ImageApiController.class);


    private final ImageService imageService;

    @PostMapping("/save")
    public ResponseEntity<?> imageUpload(@RequestPart String caption, @RequestPart(value = "file", required = false) MultipartFile file, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 업로드
        ResponseDto<?> uploadImage = imageService.upload(caption, file, principalDetails);

        logger.info("이미지 업로드 완료");
        return new ResponseEntity<>(uploadImage, HttpStatus.OK);
    }


}
