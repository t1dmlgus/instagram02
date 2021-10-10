package com.s1dmlgus.instagram02.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentRequest;
import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@Transactional
@ExtendWith(RestDocumentationExtension.class)    // JUnit5 필수
@SpringBootTest
public class AuthApiControllerIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    @DisplayName("회원가입 요청 테스트")
    @Test
    public void joinTest() throws Exception{
        //given
        JoinRequestDto joinRequestDto = createJoinDto();
        String json = new ObjectMapper().writeValueAsString(joinRequestDto);

        //when(테스트)
        ResultActions resultAction = mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)

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
                ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 소시지"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("회원번호"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("권한")
                        )
                ));


    }




    private JoinRequestDto createJoinDto(){

        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("t1dmlgus22");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("dmlgusgngl@gmail.com");
        joinRequestDto.setName("이의현");

        return joinRequestDto;
    }


}
