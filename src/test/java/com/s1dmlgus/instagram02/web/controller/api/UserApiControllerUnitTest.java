package com.s1dmlgus.instagram02.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1dmlgus.instagram02.config.SecurityConfig;
import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.user.Gender;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentRequest;
import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(roles = "USER")
@WebMvcTest(value = UserApiController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class UserApiControllerUnitTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    // BDDMockito 패턴
    @DisplayName("회원정보 수정 테스트")
    @Test
    public void userUpdateTest() throws Exception{
        //given
        UserUpdateRequestDto updateRequestDto = createUpdateRequestDto();
        String json = new ObjectMapper().writeValueAsString(updateRequestDto);

        //when
        when(userService.update(1L, updateRequestDto)).thenReturn(new ResponseDto<>("회원 수정이 완료되었습니다.", null));

        //then
        ResultActions resultActions = mockMvc.perform(
                put("/api/user/update/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 수정이 완료되었습니다."))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}", getDocumentRequest(), getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("website").type(JsonFieldType.STRING).description("웹사이트"),
                                fieldWithPath("bio").type(JsonFieldType.STRING).description("자기소개"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별")

                        )
                ));


    }

    private JoinRequestDto createJoinDto() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("t1dmlgus");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("dmlgusgngl@mgail.com");
        joinRequestDto.setName("이의현");
        return joinRequestDto;
    }


    // 회원정보수정 DTO
    private UserUpdateRequestDto createUpdateRequestDto() {
        return UserUpdateRequestDto.builder()
                .name("이의현")
                .website("github/t1dmlgus.com")
                .bio("안녕하세요")
                .phone("01022")
                .gender("남")
                .build();
    }



}