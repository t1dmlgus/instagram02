package com.s1dmlgus.instagram02.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)    // JUnit5 필수
@WebMvcTest(AuthApiController.class)
class AuthApiControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean   // IoC 환경에 bean 등록됨
    private UserService userService;


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
                post("http://localhost:8080/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );


        //then(검증)
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 정상적으로 되었습니다."))
                .andDo(MockMvcResultHandlers.print());
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
        ResultActions resultAction = mockMvc.perform(
                post("http://localhost:8080/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
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
        joinRequestDto.setUsername("22222222");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("gngl@mgail.com");
        joinRequestDto.setName("t1dmlgus");
        return joinRequestDto;
    }


    private JoinRequestDto createExceptionJoinDto() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("2222222222222222222222222222222222222");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("gngl@mgail.com");
        joinRequestDto.setName("t1dmlgus");
        return joinRequestDto;
    }

}