package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
@ExtendWith(MockitoExtension.class)
class S3ServiceTest {


    @InjectMocks
    private S3Service s3Service;



    @DisplayName("s3 업로드 실패 테스트")
    @Test
    public void failS3UploadTest() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("테스트파일", "테스트파일명.jpeg", "image/jpeg", "<<테스트파일>>".getBytes());
        ImageUploadDto imageUploadDto = new ImageUploadDto("1L", "이미지업로드테스트입니다", file);
        String fileName = "uuid_테스트명";

        //when

        //then
        assertThatThrownBy(() -> s3Service.upload(imageUploadDto, fileName))
                .isInstanceOf(Exception.class)
                .hasMessage("s3 이미지 업로드에 실패하였습니다.");


    }


}