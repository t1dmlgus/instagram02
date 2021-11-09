package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.ImageService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageApiController {

    private final ImageService imageService;

    @PostMapping("/save")
    public ResponseEntity<?> imageUpload(@RequestPart String caption, @RequestPart(value = "file", required = false) MultipartFile file, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 업로드
        imageService.upload(caption,file, principalDetails);




        return new ResponseEntity<>(new ResponseDto<>("이미지가 업로드 되었습니다.", null), HttpStatus.OK);
    }


}
