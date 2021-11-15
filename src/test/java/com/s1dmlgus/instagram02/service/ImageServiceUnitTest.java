package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.domain.image.ImageRepository;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.ImageUploadDto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Transactional
@ExtendWith(MockitoExtension.class)
class ImageServiceUnitTest {

    @InjectMocks
    private ImageService imageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserRepository userRepository;

    private User user01;


    @BeforeEach
    public void setUp(){
        user01 = User.builder()
                .username("t1dmlgus")
                .password("1234")
                .email("dmlgus@gamil.com")
                .name("이의현")
                .build();
    }



    @DisplayName("이미지 업로드 테스트")
    @Test
    public void imageUploadTest() throws Exception {

        //given
        MockMultipartFile file = new MockMultipartFile("파일제목", "파일제목.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        ImageUploadDto imageUploadDto = new ImageUploadDto("1L", "이미지업로드테스트입니다", file);
        when(userRepository.findById(Long.parseLong(imageUploadDto.getUserId()))).thenReturn(Optional.of(user01));
        when(imageRepository.save(any())).thenReturn(imageUploadDto.toEntity("uuid_fileName", user01));

        //when
        ResponseDto<?> upload = imageService.upload(imageUploadDto);

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


    @DisplayName("파일 유효성검사")
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

}
