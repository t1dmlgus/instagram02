package com.s1dmlgus.instagram02.web.controller.api;

import com.s1dmlgus.instagram02.config.SecurityConfig;
import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.config.auth.WithMockCustomUser;
import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.service.ImageService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.story.ImageStoryResponseDto;
import com.s1dmlgus.instagram02.web.dto.image.upload.ImageUploadRequestDto;
import com.s1dmlgus.instagram02.web.dto.image.upload.ImageUploadResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentRequest;
import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)    // JUnit5 필수
@WebMvcTest(value = ImageApiController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class ImageApiControllerUnitTest {

    private MockMvc mockMvc;

    @MockBean   // IoC 환경에 bean 등록됨
    private ImageService imageService;

    private PrincipalDetails principal;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        principal = (PrincipalDetails) authentication.getPrincipal();
    }


    @DisplayName("이미지 업로드 테스트")
    @WithMockCustomUser
    @Test
    public void uploadImageTest() throws Exception{
        //given
        MockMultipartFile files = new MockMultipartFile("file", "파일명.jpeg", "image/jpeg", "<<파일데이터>>".getBytes());
        //ImageUploadRequestDto imageUploadRequestDto = new ImageUploadRequestDto("1L", "테스트이미지입니다", files);
        Image afterUploadImage = new Image(1L, "테스트이미지입니다", "https://d3r3itann8ixvx.cloudfront.net/98e40ae0-1aa1-4006-8ae5-f258aa0e6f01_1d42ea960de1c884866c8656a1b5031b.jpg", principal.getUser());


        doReturn(new ResponseDto<>("이미지가 업로드 되었습니다.", new ImageUploadResponseDto(afterUploadImage)))
                .when(imageService).upload(any(ImageUploadRequestDto.class), any(PrincipalDetails.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/api/image/save")
                        .file(files)
                        .param("userId", "1L")
                        .param("caption", "이미지업로드테스트입니다")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON_UTF8)

        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}", getDocumentRequest(), getDocumentResponse(),
                        requestParameters(
                                parameterWithName("caption").description("이미지 타이틀"),
                                parameterWithName("userId").description("유저 아이디")
                        )


                ));

    }


    @DisplayName("스토리 가져오기 테스트")
    @WithMockCustomUser
    @Test
    public void getStoryTest() throws Exception{
        //given

        List<Image> testAr = new ArrayList<>();
        testAr.add(new Image(1L, "테스트이미지01", "31a06720-7e1b-4388-a241-4e246c6a94b8_1.jpg", principal.getUser()));
        testAr.add(new Image(2L, "테스트이미지02", "31a06720-7e1b-4388-a241-4e246c6a94b8_2.jpg", principal.getUser()));


        doReturn(new ResponseDto<>("이미지 스토리를 가져옵니다.", ImageStoryResponseDto.imageStoryResponseDtoList(testAr, 0)))
                .when(imageService).getStory(principal);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .characterEncoding("utf-8")
        );

        //then
         resultActions
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print())
                        .andDo(document("{class-name}/{method-name}", getDocumentRequest(), getDocumentResponse()
                        ));

    }
    
}