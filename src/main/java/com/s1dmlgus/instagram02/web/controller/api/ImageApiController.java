package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.ImageService;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;


@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageApiController {

    private final ImageService imageService;

    @PostMapping("/save")
    public ResponseEntity<?> imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        // 업로드
        imageService.upload(imageUploadDto, principalDetails);

        return new ResponseEntity<>( "이미지가 업로드 되었습니다.", HttpStatus.OK);
    }


}
