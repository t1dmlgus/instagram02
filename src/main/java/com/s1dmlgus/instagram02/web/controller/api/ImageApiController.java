package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.ImageService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.upload.ImageUploadRequestDto;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;


@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageApiController {

    Logger logger = LoggerFactory.getLogger(ImageApiController.class);

    private final ImageService imageService;


    // 스토리 가져오기
    @GetMapping()
    public ResponseEntity<?> story(@AuthenticationPrincipal PrincipalDetails principalDetails){

        ResponseDto<?> story = imageService.getStory(principalDetails);
        logger.info("story : {}", story);

        return new ResponseEntity<>(story, HttpStatus.OK);
    }


    // 업로드
    @PostMapping("/save")
    public ResponseEntity<?> imageUpload(@Valid ImageUploadRequestDto imageUploadRequestDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        logger.info("immageUploadDto {}", imageUploadRequestDto);

        // 업로드
        ResponseDto<?> uploadImage = imageService.upload(imageUploadRequestDto, principalDetails);

        logger.info("[이미지 업로드 완료] uploadImage : {}", uploadImage);

        return new ResponseEntity<>(uploadImage, HttpStatus.OK);

    }


    // MultipartFile 바인딩
    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                logger.info("initBinder MultipartFile.class: {}; set null;", text);
                setValue(null);
            }

        });
    }



}
