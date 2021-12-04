package com.s1dmlgus.instagram02.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1dmlgus.instagram02.config.SecurityConfig;
import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.config.auth.WithMockCustomUser;
import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.handler.exception.CustomValidationException;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import com.s1dmlgus.instagram02.web.dto.user.profile.UserProfileResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.update.UserUpdateResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentRequest;
import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(value = UserApiController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class UserApiControllerUnitTest {

    Logger logger = LoggerFactory.getLogger(UserApiControllerUnitTest.class);

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private PrincipalDetails principal;


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

    }


    @DisplayName("회원가입 테스트")
    @Test
    public void joinUserTest() throws Exception{
        //given
        JoinRequestDto joinRequestDto = new JoinRequestDto("t1dmlgus", "1234", "test@gamil.com", "이의현");
        String json = new ObjectMapper().writeValueAsString(joinRequestDto);

        User joinUser = User.builder()
                .id(1L)
                .username("t1dmlgus")
                .email("test@gmail.com")
                .name("이의현")
                .build();
        joinUser.setRole(Role.ROLE_USER);


        //when
        doReturn(new ResponseDto<>("회원가입이 정상적으로 되었습니다.", new JoinResponseDto(joinUser)))
                .when(userService).join(joinRequestDto);


        ResultActions resultActions = mockMvc.perform(
                post("/api/user/join")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
        );


        //then(검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 정상적으로 되었습니다."))
                .andDo(print())
                .andDo(document("{class-name}/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                        )
                ));

    }



    @DisplayName("회원가입 username 유효성 검사 예외 처리 테스트")
    @Test
    public void joinDtoUsernameExceptionTest() throws Exception {

        //given(준비)
        JoinRequestDto joinRequestDto = new JoinRequestDto("t22222222222222222222222222222222222dmlgus", "1234", "test@gamil.com", "테스트의현");
        String json = new ObjectMapper().writeValueAsString(joinRequestDto);

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("username", "0에서 20자까지 작성할 수 있습니다");

        // new ResponseDto -> data 가 안담긴다.
        when(userService.join(joinRequestDto)).thenThrow(new CustomValidationException("유효성 검사 실패", errorMap));

        //when(테스트)
        ResultActions resultAction =
                mockMvc.perform(
                        post("/api/user/join")
                                .with(csrf())
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)

                );

        //then(검증)
        resultAction
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효성 검사 실패"))
                .andExpect(jsonPath("$.data.username").value("0에서 20자까지 작성할 수 있습니다"))
                .andDo(MockMvcResultHandlers.print());

    }


    
    @DisplayName("프로필 유저 조회 테스트")
    @WithMockCustomUser()
    @Test
    public void getProfileUserTest() throws Exception{
        //given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        principal = (PrincipalDetails) authentication.getPrincipal();

        User user = User.builder()
                .id(2L)
                .username("t1dmlgus")
                .email("test@gmail.com")
                .name("이의현")
                .bio("테스트입니다")
                .website("https://test.com")
                .build();


        long pageId = 2L;

        doReturn(new ResponseDto<>("회원 정보를 조회합니다.", new UserProfileResponseDto(user, true, false, 0)))
                .when(userService).profile(pageId, principal);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/user/{id}",pageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .characterEncoding("utf-8")

        );

        //then
        resultActions
                //.andExpect(status().isOk())
                //.andExpect(jsonPath("$.message").value("회원 정보를 조회합니다."))
                .andDo(print())
                .andDo(document("{class-name}/{method-name}",getDocumentRequest(), getDocumentResponse()
        ));

    }

    
    // BDDMockito 패턴
    @DisplayName("회원정보 수정 테스트")
    @Test
    @WithMockCustomUser()
    public void updateUserTest() throws Exception{
        //given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        principal = (PrincipalDetails) authentication.getPrincipal();

        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto("이의현", "update.com", "업데이트요!", "03033", "남");
        String json = new ObjectMapper().writeValueAsString(updateRequestDto);

        User updateUser = User.builder()
                .id(principal.getUser().getId())
                .username(principal.getUsername())
                .name(updateRequestDto.getName())
                .website(updateRequestDto.getWebsite())
                .bio(updateRequestDto.getBio())
                .phone(updateRequestDto.getPhone())
                .build();


        doReturn(new ResponseDto<>("회원 수정이 완료되었습니다.", new UserUpdateResponseDto(updateUser)))
                .when(userService).update(updateRequestDto, principal);



        //when
        ResultActions resultActions = mockMvc.perform(
                put("/api/user/update")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 수정이 완료되었습니다."))
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", getDocumentRequest(), getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("website").type(JsonFieldType.STRING).description("웹사이트"),
                                fieldWithPath("bio").type(JsonFieldType.STRING).description("자기소개"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰번호"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별")
                        )
                ));

    }

}