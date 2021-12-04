package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.image.ImageRepository;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.upload.ImageUploadRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;



@Transactional
@ExtendWith(MockitoExtension.class)
class ImageServiceUnitTest {

    Logger logger = LoggerFactory.getLogger(ImageServiceUnitTest.class);


    @Spy
    @InjectMocks
    private ImageService imageService;
    @Mock
    private S3Service s3Service;

    @Mock
    private ImageRepository imageRepository;

    private User user;

    @BeforeEach
    public void setUp(){

        ReflectionTestUtils.setField(imageService, "uploadFolder", "C:/workspace/springbootwork/upload/");

        user = User.builder()
                .id(1L)
                .username("testID")
                .email("test@gmail.com")
                .name("이의현")
                .build();
    }



    @DisplayName("이미지 업로드 테스트")
    @Test
    public void imageUploadTest() throws Exception {

        //given
        MockMultipartFile file = new MockMultipartFile("테스트파일", "테스트파일명.jpeg", "image/jpeg", "<<테스트파일>>".getBytes());
        ImageUploadRequestDto imageUploadRequestDto = new ImageUploadRequestDto("1L", "이미지업로드테스트입니다", file);


        when(imageService.createFile(imageUploadRequestDto)).thenReturn("uuid_테스트명");
        doNothing().when(s3Service).upload(any(), eq("uuid_테스트명"));
        doReturn(new Image(1L, "이미지업로드테스트입니다", "https://d3r3itann8ixvx.cloudfront.net/uuid_테스트명", user))
                .when(imageRepository).save(any());
        
        //when
        ResponseDto<?> upload = imageService.upload(imageUploadRequestDto, new PrincipalDetails(user));

        logger.info("upload : {}", upload);

        //then
        assertThat(upload.getMessage()).isEqualTo("이미지가 업로드 되었습니다.");

    }

    
    @DisplayName("파일 생성 테스트")
    @Test
    public void createFileTest() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("파일제목", "파일제목.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        ImageUploadRequestDto imageUploadRequestDto = new ImageUploadRequestDto("1L", "이미지업로드테스트입니다", file);

        //when
        String fileName = imageService.createFile(imageUploadRequestDto);

        //then
        assertThat(fileName).contains("파일제목.jpeg");

    }


    @DisplayName("파일 유효성검사 테스트")
    @Test
    public void validationFileTest() throws Exception{
        //given
        MockMultipartFile file = null;
        ImageUploadRequestDto imageUploadRequestDto = new ImageUploadRequestDto("1L", "이미지업로드테스트입니다", file);

        //when

        //then
        assertThatThrownBy(() -> imageService.validationFile(imageUploadRequestDto))
                .isInstanceOf(CustomApiException.class)
                .hasMessage("이미지가 첨부되지 않았습니다.");

    }
    
    @DisplayName("파일 업로드 실패 테스트")
    @Test
    public void uploadFileTest() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("파일제목", "파일제목.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        ImageUploadRequestDto imageUploadRequestDto = new ImageUploadRequestDto("1L", "이미지업로드테스트입니다", null);

        //when

        
        //then
        assertThatThrownBy(() -> imageService.uploadFile(imageUploadRequestDto, "hi"))
                .isInstanceOf(CustomApiException.class)
                .hasMessage("이미지 업로드에 실패했습니다.");

    }

    @DisplayName("스토리 가져오기 테스트")
    @Test
    public void storyTest() throws Exception{
        //given
        List<Image> testAr = new ArrayList<>();
        testAr.add(new Image(1L, "테스트01", "31a06720-7e1b-4388-a241-4e246c6a94b8_1.jpg", User.builder().username("t2dmlgus").build()));
        testAr.add(new Image(2L, "테스트02", "31a06720-7e1b-4388-a241-4e246c6a94b8_2.jpg", User.builder().username("t3dmlgus").build()));

        when(imageRepository.getStory(anyLong())).thenReturn(testAr);

        //when
        ResponseDto<?> story = imageService.getStory(new PrincipalDetails(user));

        logger.info("story : {}", story);

        //then
        Assertions.assertThat(story.getMessage()).isEqualTo("이미지 스토리를 가져옵니다.");

    }


}
