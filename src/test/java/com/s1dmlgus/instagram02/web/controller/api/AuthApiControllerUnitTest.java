package com.s1dmlgus.instagram02.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthApiController.class)
class AuthApiControllerUnitTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean   // IoC 환경에 bean 등록됨
    private UserService userService;

    
    // BDDMockito 패턴
    @DisplayName("회원가입 단위 테스트")
    @Test
    public void joinTest() throws Exception {

        //given(준비)
        JoinDto joinDto = createJoinDto();
        String json = new ObjectMapper().writeValueAsString(joinDto);
    
        // new ResponseDto -> data 가 안담긴다.
        when(userService.join(joinDto)).thenReturn(new ResponseDto<>("회원가입이 정상적으로 되었습니다.", null));
        

        //when(테스트)
        ResultActions resultAction = mockMvc.perform(
                post("http://localhost:8080/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        );


        //then(검증)
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 정상적으로 되었습니다."))
                .andDo(MockMvcResultHandlers.print());


    }


    @DisplayName("회원가입 유효성 검사 예외 테스트")
    @Test
    public void joinDtoExceptionTest() throws Exception {

        //given(준비)
        JoinDto joinDto = createExceptionJoinDto();
        String json = new ObjectMapper().writeValueAsString(joinDto);

        // new ResponseDto -> data 가 안담긴다.
        when(userService.join(joinDto)).thenReturn(new ResponseDto<>("회원가입이 정상적으로 되었습니다.", null));


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
                .andExpect(jsonPath("$.message").value("유효성 검사 실021패"))
                .andExpect(jsonPath("$.data.username").value("0에서 20자까지 작성할 수 있습니다"))
                .andDo(MockMvcResultHandlers.print());


    }
    private JoinDto createJoinDto() {
        JoinDto joinDto = new JoinDto();
        joinDto.setUsername("22222222");
        joinDto.setPassword("1234");
        joinDto.setEmail("gngl@mgail.com");
        joinDto.setName("t1dmlgus");
        return joinDto;
    }


    private JoinDto createExceptionJoinDto() {
        JoinDto joinDto = new JoinDto();
        joinDto.setUsername("2222222222222222222222222222222222222");
        joinDto.setPassword("1234");
        joinDto.setEmail("gngl@mgail.com");
        joinDto.setName("t1dmlgus");
        return joinDto;
    }

}