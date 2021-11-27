package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.image.ImageRepository;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@Transactional
@ExtendWith(MockitoExtension.class)
class ImageServiceUnitTest {


    @Spy
    @InjectMocks
    private ImageService imageService;
    @Mock
    private S3Service s3Service;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PrincipalDetails principalDetails;


    @BeforeEach
    public void setUp(){
        User user = User.builder()
                .username("t1dmlgus")
                .password("1234")
                .email("dmlgus@gamil.com")
                .name("이의현")
                .build();

        principalDetails.setUser(user);

        ReflectionTestUtils.setField(imageService, "uploadFolder", "C:/workspace/springbootwork/upload/");
    }



    @DisplayName("이미지 업로드 테스트")
    @Test
    public void imageUploadTest() throws Exception {

        //given
        MockMultipartFile file = new MockMultipartFile("테스트파일", "테스트파일명.jpeg", "image/jpeg", "<<테스트파일>>".getBytes());
        ImageUploadDto imageUploadDto = new ImageUploadDto("1L", "이미지업로드테스트입니다", file);


        when(imageService.createFile(imageUploadDto)).thenReturn("uuid_테스트명");
        doNothing().when(s3Service).upload(any(), eq("uuid_테스트명"));
        when(imageRepository.save(any())).thenReturn(imageUploadDto.toEntity(eq("uuid_테스트명"),any()));
        
        //when
        ResponseDto<?> upload = imageService.upload(imageUploadDto, principalDetails);

        //then
        assertThat(upload.getMessage()).isEqualTo("이미지가 업로드 되었습니다.");
        
    }

    
    @DisplayName("파일 생성 테스트")
    @Test
    public void createFileTest() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("파일제목", "파일제목.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        ImageUploadDto imageUploadDto = new ImageUploadDto("1L", "이미지업로드테스트입니다", file);

        //when
        String fileName = imageService.createFile(imageUploadDto);

        //then
        assertThat(fileName).contains("파일제목.jpeg");

    }


    @DisplayName("파일 유효성검사 테스트")
    @Test
    public void validationFileTest() throws Exception{
        //given
        MockMultipartFile file = null;
        ImageUploadDto imageUploadDto = new ImageUploadDto("1L", "이미지업로드테스트입니다", file);

        //when

        //then
        assertThatThrownBy(() -> imageService.validationFile(imageUploadDto))
                .isInstanceOf(CustomApiException.class)
                .hasMessage("이미지가 첨부되지 않았습니다.");

    }
    
    @DisplayName("파일 업로드 실패 테스트")
    @Test
    public void uploadFileTest() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("파일제목", "파일제목.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        ImageUploadDto imageUploadDto = new ImageUploadDto("1L", "이미지업로드테스트입니다", null);

        //when

        
        //then
        assertThatThrownBy(() -> imageService.uploadFile(imageUploadDto, "hi"))
                .isInstanceOf(CustomApiException.class)
                .hasMessage("이미지 업로드에 실패했습니다.");

    }

}
