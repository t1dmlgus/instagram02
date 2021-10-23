package com.s1dmlgus.instagram02.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1dmlgus.instagram02.config.SecurityConfig;
import com.s1dmlgus.instagram02.handler.exception.CustomValidationException;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.HashMap;
import java.util.Map;

import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentRequest;
import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)    // JUnit5 필수
@WithMockUser(roles = "USER")
@WebMvcTest(value = AuthApiController.class, excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class AuthApiControllerUnitTest {

    private MockMvc mockMvc;

    @MockBean   // IoC 환경에 bean 등록됨
    private UserService userService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    // BDDMockito 패턴
    @DisplayName("회원가입 컨트롤러 단위 테스트")
    @Test
    public void joinTest() throws Exception {

        //given(준비)
        JoinRequestDto joinRequestDto = createJoinDto();
        String json = new ObjectMapper().writeValueAsString(joinRequestDto);

        // new ResponseDto -> data 가 안담긴다.
        when(userService.join(joinRequestDto)).thenReturn(new ResponseDto<>("회원가입이 정상적으로 되었습니다.", null));


        //when(테스트)
        ResultActions resultAction = mockMvc.perform(
                post("/api/auth/signup")
                        .with(csrf())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        );

        //then(검증)
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 정상적으로 되었습니다."))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("넥네임"),
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
        JoinRequestDto joinRequestDto = createExceptionJoinDto();
        String json = new ObjectMapper().writeValueAsString(joinRequestDto);

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("username", "0에서 20자까지 작성할 수 있습니다");

        // new ResponseDto -> data 가 안담긴다.
        when(userService.join(joinRequestDto)).thenThrow(new CustomValidationException("유효성 검사 실패", errorMap));

        //when(테스트)
        ResultActions resultAction =
                mockMvc.perform(
                post("/api/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)

        );

        //then(검증)
        resultAction
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효성 검사 실패"))
                .andExpect(jsonPath("$.data.username").value("0에서 20자까지 작성할 수 있습니다"))
                .andDo(MockMvcResultHandlers.print());

    }

    private JoinRequestDto createJoinDto() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("t1dmlgus");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("dmlgusgngl@mgail.com");
        joinRequestDto.setName("이의현");
        return joinRequestDto;
    }


    private JoinRequestDto createExceptionJoinDto() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("t22222222222222222222222222222222222dmlgus");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("dmlgusgngl@mgail.com");
        joinRequestDto.setName("이의현");
        return joinRequestDto;
    }

}